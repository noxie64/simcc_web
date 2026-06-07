package at.simcc.simcc_backend.api.service;

import at.simcc.simcc_backend.entities.Trojan;
import at.simcc.simcc_backend.entities.User;
import at.simcc.simcc_backend.entities.trojan_setting.TrojanSetting;
import at.simcc.simcc_backend.entities.trojan_setting.TrojanSettingKey;
import at.simcc.simcc_backend.repo.TrojanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Project: backend
 * Created by: Georg Kollegger
 * Date: 5/26/26
 */
@Service
@RequiredArgsConstructor
public class TrojanService {
    private final TrojanRepository trojanRepository;

    public void createTrojan(String name, Map<TrojanSettingKey, Object> buildConfig, User user) {

        List<TrojanSetting> trojanSettings = new ArrayList<>();
        for (TrojanSettingKey key : TrojanSettingKey.values()) {

            if (buildConfig != null && buildConfig.containsKey(key)) {
                trojanSettings.add(new TrojanSetting().builder()
                        .key(key)
                        .value(String.valueOf(buildConfig.get(key)))
                        .build()
                );
            } else {

                trojanSettings.add(new TrojanSetting().builder()
                        .key(key)
                        .value(String.valueOf(key.defaultValue))
                        .build()
                );
            }
        }

        Trojan trojan = new Trojan()
                .builder()
                .name(name)
                .createdBy(user)
                .trojanSettings(trojanSettings)
                .build();
        trojan.getTrojanSettings().forEach(t -> t.setTrojan(trojan));

        trojanRepository.save(trojan);
    }
}
