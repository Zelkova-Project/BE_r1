package com.zelkova.zelkova.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {
    
    @Bean
    public ModelMapper getMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                    .setFieldMatchingEnabled(true) // 필드 값과 맞는것 끼리 매칭시킬지 여부 값 셋팅
                    .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE) // 어떤 필드타입에 접근해서 맞춰줄건지 셋팅
                    .setMatchingStrategy(MatchingStrategies.LOOSE); // matching을 루즈하게 해준다. (권장사항임 필드값 하나라도 안맞으면 에러남)

        return modelMapper;
    }
}
