package com.devpies.devpiesback.core.rest.services.interfaces;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Patient;
import com.devpies.devpiesback.auth.application.domain.model.roles.Representative;

public interface IRepresentativeService {
    Boolean deleteRepresentative(User user);
    Boolean editRepresentative(User user, Representative representative);
    Representative getRepresentativeByUser(User user);
}
