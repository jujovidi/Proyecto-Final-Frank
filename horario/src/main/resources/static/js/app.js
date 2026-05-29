const API_BASE = '/api/horario/materias';

const DIAS = ['LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO'];
const DIAS_LABEL = ['Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes', 'Sabado'];
const HORA_INICIO = 7;
const HORA_FIN = 21;

let materias = [];
let editingId = null;
let colorIndex = 0;
let deleteTargetId = null;

// DOM refs
const calendarGrid = document.getElementById('calendar-grid');
const modalOverlay = document.getElementById('modal-overlay');
const modalTitle = document.getElementById('modal-title');
const materiaForm = document.getElementById('materia-form');
const materiaId = document.getElementById('materia-id');
const materiaNombre = document.getElementById('materia-nombre');
const materiaDocente = document.getElementById('materia-docente');
const materiaSalon = document.getElementById('materia-salon');
const horariosContainer = document.getElementById('horarios-container');

document.getElementById('btnAgregarMateria').addEventListener('click', () => abrirModal());
document.getElementById('btnCerrarModal').addEventListener('click', cerrarModal);
document.getElementById('btnCancelar').addEventListener('click', cerrarModal);
document.getElementById('btnAgregarHorario').addEventListener('click', agregarHorarioEntry);
materiaForm.addEventListener('submit', guardarMateria);
modalOverlay.addEventListener('click', (e) => { if (e.target === modalOverlay) cerrarModal(); });

document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') cerrarModal();
});

function init() {
    renderCalendar();
    cargarMaterias();
}

function renderCalendar() {
    calendarGrid.innerHTML = '';

    calendarGrid.appendChild(crearCelda('', 'calendar-header'));

    for (let i = 0; i < DIAS.length; i++) {
        const h = crearCelda(DIAS_LABEL[i], 'calendar-header');
        calendarGrid.appendChild(h);
    }

    for (let h = HORA_INICIO; h < HORA_FIN; h++) {
        const timeStr = `${String(h).padStart(2, '0')}:00`;
        const label = crearCelda(timeStr, 'time-label');
        calendarGrid.appendChild(label);

        for (let d = 0; d < DIAS.length; d++) {
            const slot = document.createElement('div');
            slot.className = 'time-slot empty';
            slot.dataset.hour = h;
            slot.dataset.dia = DIAS[d];
            slot.title = `${DIAS_LABEL[d]} ${timeStr}`;
            slot.addEventListener('click', () => {
                abrirModal(null, DIAS[d], timeStr);
            });
            calendarGrid.appendChild(slot);
        }
    }
}

function crearCelda(texto, className) {
    const el = document.createElement('div');
    el.className = className;
    el.textContent = texto;
    return el;
}

async function cargarMaterias() {
    try {
        const res = await fetch(`${API_BASE}/all`);
        if (!res.ok) throw new Error('Error al cargar materias');
        materias = await res.json();
        renderizarMaterias();
    } catch (err) {
        console.error(err);
        alert('Error al cargar las materias');
    }
}

function renderizarMaterias() {
    document.querySelectorAll('.materia-card').forEach(el => el.remove());

    colorIndex = 0;
    const colorMap = {};

    for (const materia of materias) {
        if (!colorMap[materia.id]) {
            colorMap[materia.id] = colorIndex++ % 10;
        }

        if (!materia.horarios) continue;

        for (const horario of materia.horarios) {
            const diaIdx = DIAS.indexOf(horario.diaSemana);
            if (diaIdx === -1) continue;

            const inicioParts = horario.horaInicio.split(':').map(Number);
            const finParts = horario.horaFin.split(':').map(Number);
            const inicioHour = inicioParts[0] + (inicioParts[1] || 0) / 60;
            const finHour = finParts[0] + (finParts[1] || 0) / 60;

            const startRow = inicioHour - HORA_INICIO;
            const endRow = finHour - HORA_INICIO;
            const rowSpan = Math.max(endRow - startRow, 0.5);

            const slotCells = document.querySelectorAll(
                `.time-slot[data-dia="${horario.diaSemana}"]`
            );

            if (startRow < 0 || startRow >= slotCells.length) continue;

            const cell = slotCells[startRow];
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
                    <button class="btn-edit-materia" data-id="${materia.id}">Editar</button>
                    <button class="btn-delete-materia" data-id="${materia.id}">Eliminar</button>
                </div>
            `;

            card.querySelector('.btn-edit-materia').addEventListener('click', (e) => {
                e.stopPropagation();
                abrirModal(materia.id);
            });

            card.querySelector('.btn-delete-materia').addEventListener('click', (e) => {
                e.stopPropagation();
                eliminarMateria(materia.id);
            });

            card.addEventListener('click', () => abrirModal(materia.id));

            cell.style.position = 'relative';
            cell.appendChild(card);
        }
    }
}

function abrirModal(materiaIdEditar, diaPrefill, horaPrefill) {
    editingId = materiaIdEditar || null;

    if (editingId) {
        modalTitle.textContent = 'Editar Materia';
        const materia = materias.find(m => m.id === editingId);
        if (!materia) return;

        materiaNombre.value = materia.nombre;
        materiaDocente.value = materia.docente;
        materiaSalon.value = materia.salon;
        materiaId.value = materia.id;

        horariosContainer.innerHTML = '';
        if (materia.horarios && materia.horarios.length > 0) {
            for (const h of materia.horarios) {
                agregarHorarioEntry(h.diaSemana, h.horaInicio, h.horaFin);
            }
        } else {
            agregarHorarioEntry();
        }
    } else {
        modalTitle.textContent = 'Nueva Materia';
        materiaForm.reset();
        materiaId.value = '';
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
    deleteTargetId = null;
}

function agregarHorarioEntry(dia, inicio, fin) {
    const entry = document.createElement('div');
    entry.className = 'horario-entry';

    entry.innerHTML = `
        <select class="horario-dia" required>
            <option value="">Dia</option>
            <option value="LUNES" ${dia === 'LUNES' ? 'selected' : ''}>Lunes</option>
            <option value="MARTES" ${dia === 'MARTES' ? 'selected' : ''}>Martes</option>
            <option value="MIERCOLES" ${dia === 'MIERCOLES' ? 'selected' : ''}>Miercoles</option>
            <option value="JUEVES" ${dia === 'JUEVES' ? 'selected' : ''}>Jueves</option>
            <option value="VIERNES" ${dia === 'VIERNES' ? 'selected' : ''}>Viernes</option>
            <option value="SABADO" ${dia === 'SABADO' ? 'selected' : ''}>Sabado</option>
        </select>
        <input type="time" class="horario-inicio" required value="${inicio || '08:00'}">
        <input type="time" class="horario-fin" required value="${fin || '10:00'}">
        <button type="button" class="btn-remove-horario" title="Eliminar horario">&times;</button>
    `;

    entry.querySelector('.btn-remove-horario').addEventListener('click', () => {
        const entries = horariosContainer.querySelectorAll('.horario-entry');
        if (entries.length > 1) {
            entry.remove();
        }
    });

    horariosContainer.appendChild(entry);
}

async function guardarMateria(e) {
    e.preventDefault();

    const id = materiaId.value.trim() || materiaNombre.value.trim().toUpperCase().replace(/\s+/g, '_');
    const nombre = materiaNombre.value.trim();
    const docente = materiaDocente.value.trim();
    const salon = materiaSalon.value.trim();

    if (!nombre || !docente || !salon) {
        alert('Todos los campos son obligatorios');
        return;
    }

    const horarioEntries = horariosContainer.querySelectorAll('.horario-entry');
    const horarios = [];

    for (const entry of horarioEntries) {
        const dia = entry.querySelector('.horario-dia').value;
        const inicio = entry.querySelector('.horario-inicio').value;
        const fin = entry.querySelector('.horario-fin').value;

        if (!dia || !inicio || !fin) {
            alert('Complete todos los horarios');
            return;
        }
        if (inicio >= fin) {
            alert(`La hora de inicio (${inicio}) debe ser menor a la de fin (${fin})`);
            return;
        }
        horarios.push({ diaSemana: dia, horaInicio: inicio, horaFin: fin });
    }

    const payload = { id, nombre, docente, salon, horarios };

    try {
        const url = editingId ? `${API_BASE}/update` : `${API_BASE}/save`;
        const res = await fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!res.ok) {
            const errMsg = await res.text();
            throw new Error(errMsg || 'Error al guardar');
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
        const res = await fetch(`${API_BASE}/${id}`, { method: 'DELETE' });
        if (!res.ok) throw new Error('Error al eliminar');
        await cargarMaterias();
    } catch (err) {
        alert('Error: ' + err.message);
    }
}

function sumarHora(hora, horas) {
    const [h, m] = hora.split(':').map(Number);
    const total = h + horas;
    return `${String(total).padStart(2, '0')}:${String(m).padStart(2, '0')}`;
}

init();
