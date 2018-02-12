package services;

import entities.Role;
import entities.SystemConfiguration;
import repository.AbstractCrudRepo;
import repository.SystemConfigurationRepository;

import java.io.IOException;

public class SystemConfigurationService {
    private AbstractCrudRepo<SystemConfiguration, String> systemConfigurationRepository;

    public SystemConfigurationService(AbstractCrudRepo<SystemConfiguration, String> systemConfigurationRepository) {
        this.systemConfigurationRepository = systemConfigurationRepository;
    }

    public boolean isEnabledAuthentication() {
        SystemConfiguration systemConfiguration = systemConfigurationRepository.findOne("1").get();
        return systemConfiguration.getEnableAuthentication();
    }

    public void updateEnableAuthentication(boolean shouldEnableAuthentication) throws IOException {
        SystemConfiguration systemConfiguration = systemConfigurationRepository.findOne("1").get();
        systemConfiguration.setEnableAuthentication(shouldEnableAuthentication);
        systemConfigurationRepository.update("1", systemConfiguration);
    }

    public Role getRoleOfCurrentUser() {
        SystemConfiguration systemConfiguration = systemConfigurationRepository.findOne("1").get();
        return systemConfiguration.getCurrentUserRol();
    }

    public void setRoleOfCurrentUser(Role rol) throws IOException {
        SystemConfiguration systemConfiguration = systemConfigurationRepository.findOne("1").get();
        systemConfiguration.setCurrentUserRol(rol);
        systemConfigurationRepository.update("1", systemConfiguration);
    }
}
