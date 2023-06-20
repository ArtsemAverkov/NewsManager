package ru.clevertec.NewsManager.common.extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import ru.clevertec.NewsManager.dto.request.CommentRequestDto;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ValidParameterResolverCommentsRequestDto implements ParameterResolver {

    private final List<CommentRequestDto> commentsDtoList = Arrays.asList(
            new CommentRequestDto(
                    1L,
                    "test_text"
    ));
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == CommentRequestDto.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return commentsDtoList.get(new Random().nextInt(commentsDtoList.size()));
    }
}

