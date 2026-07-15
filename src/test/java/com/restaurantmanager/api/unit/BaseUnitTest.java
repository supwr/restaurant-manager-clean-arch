package com.restaurantmanager.api.unit;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Base class for unit tests.
 * Provides Mockito support for mocking dependencies.
 * These tests do NOT connect to the database and all dependencies must be mocked.
 */
@ExtendWith(MockitoExtension.class)
public abstract class BaseUnitTest {
}

