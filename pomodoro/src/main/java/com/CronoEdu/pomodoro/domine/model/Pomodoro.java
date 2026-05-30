package com.CronoEdu.pomodoro.domine.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pomodoro {

    private String id;
    private String estudianteCedula;
    private Integer ciclosCompletados;

}
