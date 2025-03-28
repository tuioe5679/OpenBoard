package com.domain.openboard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;


@Configuration      // 설정 클래스 Spring이 클래스를 Configuration으로 인식하여 Bean 등록 등을 처리
@PropertySources({  // 여러 개의 프로퍼티 파일을 한 번에 불러올 수 있게 해주는 애너테이션
                    // classpath 하위의 properties/env.properties 파일을 불러오겠다는 설정
        @PropertySource("classpath:properties/env.properties")
})
public class PropertyConfig {
}