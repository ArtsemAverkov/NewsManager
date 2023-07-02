package ru.clevertec.NewsManager.common.extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import ru.clevertec.NewsManager.dto.response.CommentResponseProtos;
import ru.clevertec.NewsManager.dto.response.NewsResponseProtos;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ValidParameterResolverNewsResponseDto implements ParameterResolver {


    List<CommentResponseProtos.CommentResponseDto> commentResponseDtoList = Arrays.asList(
            CommentResponseProtos.CommentResponseDto.newBuilder()
                    .setTime(String.valueOf(LocalDateTime.now()))
                    .setText("text_comment")
                    .setUsername( "user")
                    .build()
    );

    private final List<NewsResponseProtos.NewsResponseDto> newsDtoList = Arrays.asList(
            NewsResponseProtos.NewsResponseDto.newBuilder()
                    .setTime(String.valueOf(LocalDateTime.now()))
                    .setTitle("test_title")
                    .setText("test_text")
                    .setAuthor("admin")
                    .addAllComments(commentResponseDtoList)
                    .build());

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == NewsResponseProtos.NewsResponseDto.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return newsDtoList.get(new Random().nextInt(newsDtoList.size()));
    }
}
