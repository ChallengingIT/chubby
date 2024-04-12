/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AssociazioneGroup implements Serializable {

    private Long record;

    private List<AssociazioneModificata> associazioni;

}
