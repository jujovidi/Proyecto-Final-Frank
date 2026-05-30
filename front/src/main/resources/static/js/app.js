const AUTH_API = 'http://localhost:8081/api/auth';
const HORARIO_API = 'http://localhost:8082/api/horario/materias';
const POMODORO_API = 'http://localhost:8084/api/pomodoro';

const DIAS = ['LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO'];
const DIAS_LABEL = ['Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes', 'Sabado'];
const HORA_INICIO = 7;
const HORA_FIN = 21;

let estudianteActual = null;
let materias = [];
let colorIndex = 0;

// Pomodoro state
let pomodoroInterval = null;
let pomodoroTiempoRestante = 25 * 60;
let pomodoroFase = 'ESTUDIO';
let pomodoroEjecutando = false;
const POMODORO_ESTUDIO = 25 * 60;
const POMODORO_DESCANSO = 5 * 60;

const $ = id => document.getElementById(id);
const viewLogin = $('view-login');
const viewRegister = $('view-register');
const viewDashboard = $('view-dashboard');
const loginForm = $('login-form');
const registerForm = $('register-form');
const materiaForm = $('materia-form');
const calendarGrid = $('calendar-grid');
const modalOverlay = $('modal-overlay');
const horariosContainer = $('horarios-container');
const headerEstudiante = $('header-estudiante');

function mostrarView(viewId) {
    document.querySelectorAll('.view').forEach(v => v.classList.remove('active'));
    $(viewId).classList.add('active');
}

// --- TABS ---
document.querySelectorAll('.tab').forEach(tab => {
    tab.addEventListener('click', () => {
        document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
        tab.classList.add('active');
        document.querySelectorAll('.tab-content').forEach(tc => tc.classList.remove('active'));
        $(`tab-${tab.dataset.tab}`).classList.add('active');
        if (tab.dataset.tab === 'pomodoro') cargarPomodoro();
        if (tab.dataset.tab === 'perfil') cargarPerfil();
    });
});

// --- AUTH ---
loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    $('login-error').textContent = '';
    try {
        const res = await fetch(`${AUTH_API}/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                correoInstitucional: $('login-correo').value,
                password: $('login-password').value
            })
        });
        if (!res.ok) {
            const err = await res.text();
            throw new Error(err || 'Credenciales invalidas');
        }
        estudianteActual = await res.json();
        sessionStorage.setItem('estudiante', JSON.stringify(estudianteActual));
        entrarAlDashboard();
    } catch (err) {
        $('login-error').textContent = err.message;
    }
});

registerForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    $('register-error').textContent = '';
    try {
        const res = await fetch(`${AUTH_API}/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                cedula: $('reg-cedula').value,
                nombre: $('reg-nombre').value,
                carrera: $('reg-carrera').value,
                ubicacionSemestral: $('reg-ubicacion').value,
                correoInstitucional: $('reg-correo').value,
                password: $('reg-password').value
            })
        });
        if (!res.ok) {
            const err = await res.text();
            throw new Error(err || 'Error al registrar');
        }
        estudianteActual = await res.json();
        sessionStorage.setItem('estudiante', JSON.stringify(estudianteActual));
        entrarAlDashboard();
    } catch (err) {
        $('register-error').textContent = err.message;
    }
});

$('btn-ir-registro').addEventListener('click', (e) => {
    e.preventDefault();
    mostrarView('view-register');
});

$('btn-ir-login').addEventListener('click', (e) => {
    e.preventDefault();
    mostrarView('view-login');
});

$('btn-cerrar-sesion').addEventListener('click', () => {
    sessionStorage.removeItem('estudiante');
    estudianteActual = null;
    detenerPomodoro();
    mostrarView('view-login');
});

function entrarAlDashboard() {
    headerEstudiante.textContent = `${estudianteActual.nombre} - ${estudianteActual.carrera} (${estudianteActual.ubicacionSemestral})`;
    mostrarView('view-dashboard');
    document.querySelector('.tab[data-tab="horario"]').click();
    cargarMaterias();
}

// --- CALENDAR ---
function renderCalendar() {
    calendarGrid.innerHTML = '';
    const h = document.createElement('div');
    h.className = 'calendar-header';
    h.textContent = 'Hora';
    calendarGrid.appendChild(h);

    for (let i = 0; i < DIAS.length; i++) {
        const d = document.createElement('div');
        d.className = 'calendar-header';
        d.textContent = DIAS_LABEL[i];
        calendarGrid.appendChild(d);
    }

    for (let hr = HORA_INICIO; hr < HORA_FIN; hr++) {
        const timeStr = `${String(hr).padStart(2, '0')}:00`;
        const label = document.createElement('div');
        label.className = 'time-label';
        label.textContent = timeStr;
        calendarGrid.appendChild(label);

        for (let d = 0; d < DIAS.length; d++) {
            const slot = document.createElement('div');
            slot.className = 'time-slot empty';
            slot.dataset.hour = hr;
            slot.dataset.dia = DIAS[d];
            slot.title = `${DIAS_LABEL[d]} ${timeStr}`;
            slot.addEventListener('click', () => {
                abrirModal(null, DIAS[d], timeStr);
            });
            calendarGrid.appendChild(slot);
        }
    }
}

async function cargarMaterias() {
    try {
        const res = await fetch(`${HORARIO_API}/estudiante/${estudianteActual.cedula}`);
        if (!res.ok) throw new Error('Error al cargar materias');
        materias = await res.json();
        renderizarMaterias();
    } catch (err) {
        console.error(err);
    }
}

function renderizarMaterias() {
    document.querySelectorAll('.materia-card').forEach(el => el.remove());
    colorIndex = 0;
    const colorMap = {};

    for (const materia of materias) {
        if (!colorMap[materia.id]) colorMap[materia.id] = colorIndex++ % 10;
        if (!materia.horarios) continue;

        for (const horario of materia.horarios) {
            const diaIdx = DIAS.indexOf(horario.diaSemana);
            if (diaIdx === -1) continue;

            const ih = horario.horaInicio.split(':').map(Number);
            const fh = horario.horaFin.split(':').map(Number);
            const inicioHour = ih[0] + ih[1] / 60;
            const finHour = fh[0] + fh[1] / 60;
            const startRow = inicioHour - HORA_INICIO;
            const endRow = finHour - HORA_INICIO;
            const rowSpan = Math.max(endRow - startRow, 0.5);

            const slots = document.querySelectorAll(`.time-slot[data-dia="${horario.diaSemana}"]`);
            if (startRow < 0 || startRow >= slots.length) continue;

            const cell = slots[startRow];
            const card = document.createElement('div');
            card.className = `materia-card color-${colorMap[materia.id]}`;
            card.style.height = `${rowSpan * 52 - 4}px`;
            card.style.top = `${(inicioHour - Math.floor(inicioHour)) * 52}px`;
            card.dataset.materiaId = materia.id;
            card.innerHTML = `
                <div class="materia-nombre">${materia.nombre}</div>
                <div class="materia-info">${horario.horaInicio} - ${horario.horaFin}</div>
                <div class="materia-info">${materia.docente} | ${materia.salon}</div>
                <div class="materia-actions">
                    <button class="btn-card-edit" data-id="${materia.id}">Editar</button>
                    <button class="btn-card-delete" data-id="${materia.id}">Eliminar</button>
                </div>`;
            card.querySelector('.btn-card-edit').addEventListener('click', e => { e.stopPropagation(); abrirModal(materia.id); });
            card.querySelector('.btn-card-delete').addEventListener('click', e => { e.stopPropagation(); eliminarMateria(materia.id); });
            card.addEventListener('click', () => abrirModal(materia.id));
            cell.style.position = 'relative';
            cell.appendChild(card);
        }
    }
}

// --- MODAL ---
$('btn-agregar-materia').addEventListener('click', () => abrirModal());
$('btn-cerrar-modal').addEventListener('click', cerrarModal);
$('btn-cancelar').addEventListener('click', cerrarModal);
$('btn-agregar-horario').addEventListener('click', () => agregarHorarioEntry());
materiaForm.addEventListener('submit', guardarMateria);
modalOverlay.addEventListener('click', e => { if (e.target === modalOverlay) cerrarModal(); });
document.addEventListener('keydown', e => { if (e.key === 'Escape') cerrarModal(); });

let editingId = null;

function abrirModal(idEditar, diaPrefill, horaPrefill) {
    editingId = idEditar || null;
    if (editingId) {
        $('modal-title').textContent = 'Editar Materia';
        const m = materias.find(x => x.id === editingId);
        if (!m) return;
        $('materia-id').value = m.id;
        $('materia-nombre').value = m.nombre;
        $('materia-docente').value = m.docente;
        $('materia-salon').value = m.salon;
        horariosContainer.innerHTML = '';
        if (m.horarios && m.horarios.length > 0) {
            m.horarios.forEach(h => agregarHorarioEntry(h.diaSemana, h.horaInicio, h.horaFin));
        } else {
            agregarHorarioEntry();
        }
    } else {
        $('modal-title').textContent = 'Nueva Materia';
        materiaForm.reset();
        $('materia-id').value = '';
        horariosContainer.innerHTML = '';
        if (diaPrefill && horaPrefill) {
            agregarHorarioEntry(diaPrefill, horaPrefill, sumarHora(horaPrefill, 2));
        } else {
            agregarHorarioEntry();
        }
    }
    modalOverlay.classList.remove('hidden');
}

function cerrarModal() {
    modalOverlay.classList.add('hidden');
    editingId = null;
}

function agregarHorarioEntry(dia, inicio, fin) {
    const entry = document.createElement('div');
    entry.className = 'horario-entry';
    entry.innerHTML = `
        <select class="horario-dia" required>
            <option value="">Dia</option>
            ${['LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO'].map(d =>
                `<option value="${d}" ${dia === d ? 'selected' : ''}>${d.charAt(0)+d.slice(1).toLowerCase()}</option>`
            ).join('')}
        </select>
        <input type="time" class="horario-inicio" required value="${inicio || '08:00'}">
        <input type="time" class="horario-fin" required value="${fin || '10:00'}">
        <button type="button" class="btn-remove-horario">&times;</button>`;
    entry.querySelector('.btn-remove-horario').addEventListener('click', () => {
        if (horariosContainer.querySelectorAll('.horario-entry').length > 1) entry.remove();
    });
    horariosContainer.appendChild(entry);
}

async function guardarMateria(e) {
    e.preventDefault();
    const id = $('materia-id').value.trim() || $('materia-nombre').value.trim().toUpperCase().replace(/\s+/g, '_');
    const nombre = $('materia-nombre').value.trim();
    const docente = $('materia-docente').value.trim();
    const salon = $('materia-salon').value.trim();

    if (!nombre || !docente || !salon) { alert('Complete todos los campos'); return; }

    const entries = horariosContainer.querySelectorAll('.horario-entry');
    const horarios = [];
    for (const entry of entries) {
        const dia = entry.querySelector('.horario-dia').value;
        const inicio = entry.querySelector('.horario-inicio').value;
        const fin = entry.querySelector('.horario-fin').value;
        if (!dia || !inicio || !fin) { alert('Complete todos los horarios'); return; }
        if (inicio >= fin) { alert(`La hora de inicio debe ser menor a la de fin`); return; }
        horarios.push({ diaSemana: dia, horaInicio: inicio, horaFin: fin });
    }

    const payload = { id, nombre, docente, salon, estudianteCedula: estudianteActual.cedula, horarios };

    try {
        const url = editingId ? `${HORARIO_API}/update` : `${HORARIO_API}/save`;
        const res = await fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        if (!res.ok) {
            const err = await res.text();
            throw new Error(err || 'Error al guardar');
        }
        cerrarModal();
        await cargarMaterias();
    } catch (err) {
        alert('Error: ' + err.message);
    }
}

async function eliminarMateria(id) {
    if (!confirm('¿Eliminar esta materia?')) return;
    try {
        const res = await fetch(`${HORARIO_API}/${id}`, { method: 'DELETE' });
        if (!res.ok) throw new Error('Error al eliminar');
        await cargarMaterias();
    } catch (err) {
        alert('Error: ' + err.message);
    }
}

function sumarHora(hora, horas) {
    const [h, m] = hora.split(':').map(Number);
    return `${String(h + horas).padStart(2, '0')}:${String(m).padStart(2, '0')}`;
}

// --- POMODORO ---
function actualizarDisplayPomodoro() {
    const min = Math.floor(pomodoroTiempoRestante / 60);
    const seg = pomodoroTiempoRestante % 60;
    $('pomodoro-timer').textContent = `${String(min).padStart(2, '0')}:${String(seg).padStart(2, '0')}`;
    $('pomodoro-fase').textContent = pomodoroFase;
}

function detenerPomodoro() {
    if (pomodoroInterval) {
        clearInterval(pomodoroInterval);
        pomodoroInterval = null;
    }
    pomodoroEjecutando = false;
    $('btn-pomodoro-iniciar').disabled = false;
    $('btn-pomodoro-iniciar').textContent = 'Iniciar';
    $('btn-pomodoro-pausa').disabled = true;
}

$('btn-pomodoro-iniciar').addEventListener('click', () => {
    if (pomodoroEjecutando) return;
    if (pomodoroInterval) clearInterval(pomodoroInterval);

    if (pomodoroTiempoRestante <= 0) {
        pomodoroTiempoRestante = POMODORO_ESTUDIO;
        pomodoroFase = 'ESTUDIO';
        actualizarDisplayPomodoro();
    }

    pomodoroEjecutando = true;
    $('btn-pomodoro-iniciar').disabled = true;
    $('btn-pomodoro-pausa').disabled = false;

    pomodoroInterval = setInterval(() => {
        pomodoroTiempoRestante--;
        actualizarDisplayPomodoro();

        if (pomodoroTiempoRestante <= 0) {
            clearInterval(pomodoroInterval);
            pomodoroInterval = null;
            pomodoroEjecutando = false;
            $('btn-pomodoro-iniciar').disabled = false;
            $('btn-pomodoro-pausa').disabled = true;

            if (pomodoroFase === 'ESTUDIO') {
                completarCicloPomodoro();
                pomodoroFase = 'DESCANSO';
                pomodoroTiempoRestante = POMODORO_DESCANSO;
                $('btn-pomodoro-iniciar').textContent = 'Iniciar Descanso';
            } else {
                pomodoroFase = 'ESTUDIO';
                pomodoroTiempoRestante = POMODORO_ESTUDIO;
                $('btn-pomodoro-iniciar').textContent = 'Iniciar';
            }
            actualizarDisplayPomodoro();
        }
    }, 1000);
});

$('btn-pomodoro-pausa').addEventListener('click', () => {
    if (!pomodoroEjecutando) return;
    clearInterval(pomodoroInterval);
    pomodoroInterval = null;
    pomodoroEjecutando = false;
    $('btn-pomodoro-iniciar').disabled = false;
    $('btn-pomodoro-iniciar').textContent = pomodoroFase === 'ESTUDIO' ? 'Reanudar' : 'Reanudar Descanso';
    $('btn-pomodoro-pausa').disabled = true;
});

$('btn-pomodoro-reiniciar').addEventListener('click', () => {
    detenerPomodoro();
    pomodoroFase = 'ESTUDIO';
    pomodoroTiempoRestante = POMODORO_ESTUDIO;
    $('btn-pomodoro-iniciar').textContent = 'Iniciar';
    actualizarDisplayPomodoro();
});

$('btn-pomodoro-reset-ciclos').addEventListener('click', async () => {
    if (!confirm('¿Reiniciar contador de ciclos?')) return;
    try {
        await fetch(`${POMODORO_API}/${estudianteActual.cedula}/reiniciar`, { method: 'POST' });
        await cargarPomodoro();
    } catch (err) {
        console.error(err);
    }
});

async function completarCicloPomodoro() {
    try {
        await fetch(`${POMODORO_API}/${estudianteActual.cedula}/completar-ciclo`, { method: 'POST' });
        await cargarPomodoro();
    } catch (err) {
        console.error(err);
    }
}

async function cargarPomodoro() {
    try {
        const res = await fetch(`${POMODORO_API}/${estudianteActual.cedula}`);
        if (res.ok) {
            const data = await res.json();
            $('pomodoro-ciclos').textContent = data.ciclosCompletados || 0;
        } else {
            $('pomodoro-ciclos').textContent = '0';
        }
    } catch (err) {
        $('pomodoro-ciclos').textContent = '0';
    }
}

// --- PERFIL ---
async function cargarPerfil() {
    try {
        const res = await fetch(`${AUTH_API}/estudiantes/${estudianteActual.cedula}`);
        if (!res.ok) throw new Error('Error al cargar perfil');
        const data = await res.json();
        $('perfil-cedula').value = data.cedula;
        $('perfil-nombre').value = data.nombre;
        $('perfil-carrera').value = data.carrera;
        $('perfil-ubicacion').value = data.ubicacionSemestral;
        $('perfil-correo').value = data.correoInstitucional;
        $('perfil-password').value = '';
        $('perfil-error').textContent = '';
        $('perfil-success').textContent = '';
    } catch (err) {
        console.error(err);
    }
}

$('perfil-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    $('perfil-error').textContent = '';
    $('perfil-success').textContent = '';
    const payload = {
        cedula: $('perfil-cedula').value,
        nombre: $('perfil-nombre').value,
        carrera: $('perfil-carrera').value,
        ubicacionSemestral: $('perfil-ubicacion').value,
        correoInstitucional: $('perfil-correo').value
    };
    const pass = $('perfil-password').value.trim();
    if (pass) payload.password = pass;

    try {
        const res = await fetch(`${AUTH_API}/estudiantes/${estudianteActual.cedula}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        if (!res.ok) {
            const err = await res.text();
            throw new Error(err || 'Error al actualizar');
        }
        const updated = await res.json();
        estudianteActual = updated;
        sessionStorage.setItem('estudiante', JSON.stringify(updated));
        headerEstudiante.textContent = `${updated.nombre} - ${updated.carrera} (${updated.ubicacionSemestral})`;
        $('perfil-success').textContent = 'Datos actualizados correctamente';
    } catch (err) {
        $('perfil-error').textContent = err.message;
    }
});

$('btn-eliminar-cuenta').addEventListener('click', async () => {
    if (!confirm('¿Estas seguro de eliminar tu cuenta? Esta accion no se puede deshacer.')) return;
    if (!confirm('¿Realmente deseas eliminar tu cuenta?')) return;
    try {
        const res = await fetch(`${AUTH_API}/estudiantes/${estudianteActual.cedula}`, { method: 'DELETE' });
        if (!res.ok) throw new Error('Error al eliminar cuenta');
        sessionStorage.removeItem('estudiante');
        estudianteActual = null;
        detenerPomodoro();
        mostrarView('view-login');
    } catch (err) {
        alert('Error: ' + err.message);
    }
});

// --- INIT ---
renderCalendar();
const stored = sessionStorage.getItem('estudiante');
if (stored) {
    estudianteActual = JSON.parse(stored);
    entrarAlDashboard();
} else {
    mostrarView('view-login');
}
