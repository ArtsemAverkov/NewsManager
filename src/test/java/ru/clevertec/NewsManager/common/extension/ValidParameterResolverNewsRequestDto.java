package ru.clevertec.NewsManager.common.extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import ru.clevertec.NewsManager.dto.request.NewsRequestDto;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ValidParameterResolverNewsRequestDto implements ParameterResolver {

    private final List<NewsRequestDto> newsDtoList = Arrays.asList(
            new NewsRequestDto(
                    "admin",
                    "title_text"
            )
    );
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == NewsRequestDto.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return newsDtoList.get(new Random().nextInt(newsDtoList.size()));
    }
}
