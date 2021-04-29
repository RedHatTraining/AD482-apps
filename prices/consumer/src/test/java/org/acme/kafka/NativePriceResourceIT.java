package org.acme.kafka;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativePriceResourceIT extends PriceResourceTest {

    // Execute the same tests but in native mode.
}