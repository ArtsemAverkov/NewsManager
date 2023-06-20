package ru.clevertec.NewsManager.common.cache;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.clevertec.NewsManager.aop.cache.LfuCache;
import ru.clevertec.NewsManager.common.extension.ValidParameterResolverCommentsRequestDto;
import ru.clevertec.NewsManager.dto.request.CommentRequestDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for the LfuCache class.
 * This class contains test methods to verify the functionality of the LfuCache class.
 * It uses the ValidParameterResolverCommentsRequestDto extension to provide valid CommentRequestDto objects for testing.
 */
@ExtendWith(ValidParameterResolverCommentsRequestDto.class)
public class LfuCacheTest {

    /**
     * Test the put and get operations of the LfuCache.
     * It verifies that an object can be successfully put into the cache and retrieved using its key.
     * @param commentRequestDto the CommentRequestDto object to be used for testing
     */
    @Test
    void testPutAndGet(CommentRequestDto commentRequestDto) {
        LfuCache<Object, Object> objectObjectLfuCache = new LfuCache<>(1);
        objectObjectLfuCache.put(commentRequestDto.getNewsId(),commentRequestDto);
        Object o = objectObjectLfuCache.get(commentRequestDto.getNewsId());
        assertEquals(commentRequestDto, o);
    }


    /**
     * Test the remove operation of the LfuCache.
     * It verifies that an object can be successfully removed from the cache using its key.
     * @param commentRequestDto the CommentRequestDto object to be used for testing
     */
    @Test
    void testRemove(CommentRequestDto commentRequestDto) {
        LfuCache<Object, Object> objectObjectLfuCache = new LfuCache<>(1);
        objectObjectLfuCache.put(commentRequestDto.getNewsId(),commentRequestDto);
        objectObjectLfuCache.remove(commentRequestDto.getNewsId());
        Object o = objectObjectLfuCache.get(commentRequestDto.getNewsId());
        assertNull(o);
    }

    /**
     * Test that a value should not be available by key when the size of the cache is greater than its capacity.
     * It verifies that a value cannot be retrieved by its key when the cache size exceeds its capacity,
     * causing the least frequently used item to be evicted.
     * @param commentRequestDto the CommentRequestDto object to be used for testing
     */
    @Test
    void testPutValueShouldNotBeAvailableByKeySizeIsGreaterThanCapacity(CommentRequestDto commentRequestDto) {
        LfuCache<Object, Object> objectObjectLfuCache = new LfuCache<>(1);
        objectObjectLfuCache.put(commentRequestDto.getNewsId(),commentRequestDto);
        objectObjectLfuCache.put(2,commentRequestDto);
        Object o = objectObjectLfuCache.get(commentRequestDto.getNewsId());
        assertNull(o);
        Object object = objectObjectLfuCache.get(2);
        assertEquals(commentRequestDto, object);
    }
}
