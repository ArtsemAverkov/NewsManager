package ru.clevertec.NewsManager.common.extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import ru.clevertec.NewsManager.dto.request.CommentRequestProtos;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ValidParameterResolverCommentsRequestDto implements ParameterResolver {

    private final List<CommentRequestProtos.CommentRequestDto> commentsDtoList = Arrays.asList(
    CommentRequestProtos.CommentRequestDto.newBuilder()
            .setNewsId( 1L)
            .setText("test_text")
            .build());

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == CommentRequestProtos.CommentRequestDto.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return commentsDtoList.get(new Random().nextInt(commentsDtoList.size()));
    }
}

