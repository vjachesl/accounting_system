package com.task.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NamingConventions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Configuration
public class AccountSystemConfiguration {
    @Bean
    public ModelMapper modelMapperBean() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(new ListConverter());
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(PRIVATE)
                .setSourceNamingConvention(NamingConventions.JAVABEANS_MUTATOR)
                .setDeepCopyEnabled(true)
                .setCollectionsMergeEnabled(true)
                .setAmbiguityIgnored(true)
                .setSkipNullEnabled(true);
        return modelMapper;
    }
    
}
