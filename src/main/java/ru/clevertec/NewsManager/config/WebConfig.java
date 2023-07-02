package ru.clevertec.NewsManager.config;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 The WebConfig class implements the WebMvcConfigurer
 interface to configure the message converters for the web application.
 */
public class WebConfig implements WebMvcConfigurer {

    /**
     Extends the list of HttpMessageConverters by adding a ProtobufHttpMessageConverter.
     @param converters The list of HttpMessageConverters to be extended.
     */
    @Override
    public void extendMessageConverters (List<HttpMessageConverter<?>> converters) {
        converters.add (new ProtobufHttpMessageConverter());
    }
}