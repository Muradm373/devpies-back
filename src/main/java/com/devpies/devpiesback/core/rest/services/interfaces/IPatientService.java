package com.devpies.devpiesback.core.rest.services.interfaces;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Patient;

public interface IPatientService {
    Boolean deletePatient(User user);
    Boolean editPatient(User user, Patient patient);
    Patient getPatientByUser(User user);
}
