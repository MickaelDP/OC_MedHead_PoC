package com.medHead.poc.services;

import com.medHead.poc.model.Patient;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface PatientServiceInterface {
    Patient initializePatient(Patient patient);
    List<Patient> getAllPatients();
    Optional<Patient> getPatientById(UUID id);
    Patient savePatient(Patient patient);
    boolean deletePatient(UUID id);
    Map<String, Integer> getSpecialityDictionary();
    void clearPatients();
}
