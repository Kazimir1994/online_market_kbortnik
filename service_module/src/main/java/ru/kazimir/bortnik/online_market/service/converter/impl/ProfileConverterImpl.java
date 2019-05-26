package ru.kazimir.bortnik.online_market.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.kazimir.bortnik.online_market.repository.model.Profile;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.model.ProfileDTO;

@Component
public class ProfileConverterImpl implements Converter<ProfileDTO, Profile> {

    @Override
    public ProfileDTO toDTO(Profile profile) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setResidentialAddress(profile.getResidentialAddress());
        profileDTO.setTelephone(profile.getTelephone());
        return profileDTO;
    }

    @Override
    public Profile fromDTO(ProfileDTO profileDTO) {
        Profile profile = new Profile();
        profile.setId(profileDTO.getId());
        profile.setResidentialAddress(profileDTO.getResidentialAddress());
        profile.setTelephone(profileDTO.getTelephone());
        return profile;
    }
}
