package ru.clevertec.NewsManager.common.extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import ru.clevertec.NewsManager.dto.request.NewsRequestProtos;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ValidParameterResolverNewsRequestDto implements ParameterResolver {

    private final List<NewsRequestProtos.NewsRequestDto> newsDtoList = Arrays.asList(
            NewsRequestProtos.NewsRequestDto.newBuilder()
                    .setTitle( "admin")
                    .setText("title_text")
                    .build()
    );
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == NewsRequestProtos.NewsRequestDto.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return newsDtoList.get(new Random().nextInt(newsDtoList.size()));
    }
}
