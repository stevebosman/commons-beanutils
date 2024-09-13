/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.beanutils2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.beanutils2.bugs.other.Jira87BeanFactory;
import org.apache.commons.collections4.map.AbstractMapTest;

/**
 * Tests {@link BeanMap}.
 */
public class BeanMapTest extends AbstractMapTest<String, Object> {

    public static class BeanThrowingExceptions extends BeanWithProperties {

        public String getValueThrowingException() {
            throw new TestException();
        }

        public void setValueThrowingException(final String value) {
            throw new TestException();
        }
    }

    public static class BeanWithProperties {
        private int someInt;
        private long someLong;
        private double someDouble;
        private float someFloat;
        private short someShort;
        private byte someByte;
        private char someChar;
        private Integer someInteger;
        private String someString;
        private Object someObject;

        public byte getSomeByteValue() {
            return someByte;
        }

        public char getSomeCharValue() {
            return someChar;
        }

        public double getSomeDoubleValue() {
            return someDouble;
        }

        public float getSomeFloatValue() {
            return someFloat;
        }

        public Integer getSomeIntegerValue() {
            return someInteger;
        }

        public int getSomeIntValue() {
            return someInt;
        }

        public long getSomeLongValue() {
            return someLong;
        }

        public Object getSomeObjectValue() {
            return someObject;
        }

        public short getSomeShortValue() {
            return someShort;
        }

        public String getSomeStringValue() {
            return someString;
        }

        public void setSomeByteValue(final byte value) {
            someByte = value;
        }

        public void setSomeCharValue(final char value) {
            someChar = value;
        }

        public void setSomeDoubleValue(final double value) {
            someDouble = value;
        }

        public void setSomeFloatValue(final float value) {
            someFloat = value;
        }

        public void setSomeIntegerValue(final Integer value) {
            someInteger = value;
        }

        public void setSomeIntValue(final int value) {
            someInt = value;
        }

        public void setSomeLongValue(final long value) {
            someLong = value;
        }

        public void setSomeObjectValue(final Object value) {
            someObject = value;
        }

        public void setSomeShortValue(final short value) {
            someShort = value;
        }

        public void setSomeStringValue(final String value) {
            someString = value;
        }
    }

    /**
     * Exception for testing exception handling.
     */
    public static class TestException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    /**
     * An object value that will be stored in the bean map as a value. Need to save this externally so that we can make sure the object instances are equivalent
     * since getSampleValues() would otherwise construct a new and different Object each time.
     **/
    private final Object objectInFullMap = new Object();

    /*
     * note to self. The getter and setter methods were generated by copying the field declarations and using the following regular expression search and
     * replace:
     *
     * From: private \(.*\) some\(.*\); To: public \1 getSome\2Value() { return some\2; } public void setSome\2Value(\1 value) { some\2 = value; }
     *
     * Also note: The sample keys and mappings were generated manually.
     */

    public BeanMapTest() {
        super("BeanMapTestCase");
    }

    @Override
    public Object[] getNewSampleValues() {
        final Object[] values = {
            Integer.valueOf(223),
            Long.valueOf(23341928234L),
            Double.valueOf(23423.34),
            Float.valueOf(213332.12f),
            Short.valueOf((short)234),
            Byte.valueOf((byte)20),
            Character.valueOf('b'),
            Integer.valueOf(232),
            "SomeNewStringValue",
            new Object(),
            null,
        };
        return values;
    }

    // note to self. The Sample keys were generated by copying the field
    // declarations and using the following regular expression search and replace:
    //
    // From:
    // private \(.*\) some\(.*\);
    // To:
    // "some\2Value",
    //
    // Then, I manually added the "class" key, which is a property that exists for
    // all beans (and all objects for that matter.
    @Override
    public String[] getSampleKeys() {
        final String[] keys = {
            "someIntValue",
            "someLongValue",
            "someDoubleValue",
            "someFloatValue",
            "someShortValue",
            "someByteValue",
            "someCharValue",
            "someIntegerValue",
            "someStringValue",
            "someObjectValue",
            "class",
        };
        return keys;
    }

    // note to self: the sample values were created manually
    @Override
    public Object[] getSampleValues() {
        final Object[] values = { Integer.valueOf(1234), Long.valueOf(1298341928234L), Double.valueOf(123423.34), Float.valueOf(1213332.12f),
                Short.valueOf((short) 134), Byte.valueOf((byte) 10), Character.valueOf('a'), Integer.valueOf(1432), "SomeStringValue", objectInFullMap,
                BeanWithProperties.class, };
        return values;
    }

    @Override
    public String[] ignoredTests() {
        // Ignore the serialization tests on collection views.
        return new String[] {
         "TestBeanMap.bulkTestMapEntrySet.testCanonicalEmptyCollectionExists",
         "TestBeanMap.bulkTestMapEntrySet.testCanonicalFullCollectionExists",
         "TestBeanMap.bulkTestMapKeySet.testCanonicalEmptyCollectionExists",
         "TestBeanMap.bulkTestMapKeySet.testCanonicalFullCollectionExists",
         "TestBeanMap.bulkTestMapValues.testCanonicalEmptyCollectionExists",
         "TestBeanMap.bulkTestMapValues.testCanonicalFullCollectionExists",
         "TestBeanMap.bulkTestMapEntrySet.testSimpleSerialization",
         "TestBeanMap.bulkTestMapKeySet.testSimpleSerialization",
         "TestBeanMap.bulkTestMapEntrySet.testSerializeDeserializeThenCompare",
         "TestBeanMap.bulkTestMapKeySet.testSerializeDeserializeThenCompare"
        };
    }

    /**
     * The mappings in a BeanMap are fixed on the properties the underlying bean has. Adding and removing mappings is not possible, thus this method is
     * overridden to return false.
     */
    @Override
    public boolean isPutAddSupported() {
        return false;
    }

    /**
     * The mappings in a BeanMap are fixed on the properties the underlying bean has. Adding and removing mappings is not possible, thus this method is
     * overridden to return false.
     */
    @Override
    public boolean isRemoveSupported() {
        return false;
    }

    @Override
    public Map<String, Object> makeFullMap() {
        // note: These values must match (i.e. .equals() must return true)
        // those returned from getSampleValues().
        final BeanWithProperties bean = new BeanWithProperties();
        bean.setSomeIntValue(1234);
        bean.setSomeLongValue(1298341928234L);
        bean.setSomeDoubleValue(123423.34);
        bean.setSomeFloatValue(1213332.12f);
        bean.setSomeShortValue((short) 134);
        bean.setSomeByteValue((byte) 10);
        bean.setSomeCharValue('a');
        bean.setSomeIntegerValue(Integer.valueOf(1432));
        bean.setSomeStringValue("SomeStringValue");
        bean.setSomeObjectValue(objectInFullMap);
        return new BeanMap(bean);
    }

    @Override
    public Map<String, Object> makeObject() {
        return new BeanMap();
    }

    public void testBeanMapClone() {
        final BeanMap map = (BeanMap) makeFullMap();
        try {
            final BeanMap map2 = (BeanMap) map.clone();

            // make sure containsKey is working to verify the bean was cloned
            // ok, and the read methods were properly initialized
            final Object[] keys = getSampleKeys();
            for (final Object key : keys) {
                assertTrue(map2.containsKey(key), "Cloned BeanMap should contain the same keys");
            }
        } catch (final CloneNotSupportedException exception) {
            fail("BeanMap.clone() should not throw a " + "CloneNotSupportedException when clone should succeed.");
        }
    }

    public void testBeanMapPutAllWriteable() {
        final BeanMap map1 = (BeanMap) makeFullMap();
        final BeanMap map2 = (BeanMap) makeFullMap();
        map2.put("someIntValue", Integer.valueOf(0));
        map1.putAllWriteable(map2);
        assertEquals(map1.get("someIntValue"), Integer.valueOf(0));
    }

    /**
     * Test that the cause of exception thrown by clear() is initialized.
     */
    public void testExceptionThrowFromClear() {
        try {
            final Object bean = Jira87BeanFactory.createMappedPropertyBean();
            final BeanMap map = new BeanMap(bean);
            map.clear();
            fail("clear() - expected UnsupportedOperationException");
        } catch (final UnsupportedOperationException e) {
            Throwable cause = null;
            try {
                cause = (Throwable) PropertyUtils.getProperty(e, "cause");
            } catch (final Exception e2) {
                fail("Retrieving the cause threw " + e2);
            }
            assertNotNull(cause, "Cause null");
            assertEquals(IllegalAccessException.class, cause.getClass(),"Cause");
        }
    }

    /**
     * Test that the cause of exception thrown by a clone() is initialized.
     */
    public void testExceptionThrowFromClone() {
        // Test cloning a non-public bean (instantiation exception)
        try {
            final Object bean = Jira87BeanFactory.createMappedPropertyBean();
            final BeanMap map = new BeanMap(bean);
            map.clone();
            fail("Non-public bean clone() - expected CloneNotSupportedException");
        } catch (final CloneNotSupportedException e) {
            Throwable cause = null;
            try {
                cause = (Throwable) PropertyUtils.getProperty(e, "cause");
            } catch (final Exception e2) {
                fail("Non-public bean - retrieving the cause threw " + e2);
            }
            assertNotNull(cause, "Non-public bean cause null");
            assertEquals(IllegalAccessException.class, cause.getClass(),"Non-public bean cause");
        }

        // Test cloning a bean that throws exception
        try {
            final BeanMap map = new BeanMap(new BeanThrowingExceptions());
            map.clone();
            fail("Setter Exception clone() - expected CloneNotSupportedException");
        } catch (final CloneNotSupportedException e) {
            Throwable cause = null;
            try {
                cause = (Throwable) PropertyUtils.getProperty(e, "cause");
            } catch (final Exception e2) {
                fail("Setter Exception - retrieving the cause threw " + e2);
            }
            assertNotNull(cause, "Setter Exception cause null");
            assertEquals(IllegalArgumentException.class, cause.getClass(),"Setter Exception cause");
        }
    }

    /**
     * Test that the cause of exception thrown by put() is initialized.
     */
    public void testExceptionThrowFromPut() {
        try {
            final Map<String, Object> map = new BeanMap(new BeanThrowingExceptions());
            map.put("valueThrowingException", "value");
            fail("Setter exception - expected IllegalArgumentException");
        } catch (final IllegalArgumentException e) {
            Throwable cause1 = null;
            Throwable cause2 = null;
            try {
                cause1 = (Throwable) PropertyUtils.getProperty(e, "cause");
                cause2 = (Throwable) PropertyUtils.getProperty(e, "cause.cause");
            } catch (final Exception e2) {
                fail("Setter exception - retrieving the cause threw " + e2);
            }
            assertNotNull(cause1, "Setter exception cause 1 null");
            assertEquals(InvocationTargetException.class, cause1.getClass(),"Setter exception cause 1");
            assertNotNull(cause2, "Setter exception cause 2 null");
            assertEquals(TestException.class, cause2.getClass(),"Setter exception cause 2");
        }
    }

    /**
     * Test the default transformers using the getTypeTransformer() method
     */
    public void testGetTypeTransformerMethod() {
        final BeanMap beanMap = new BeanMap();
        assertEquals(Boolean.TRUE, beanMap.getTypeTransformer(Boolean.TYPE).apply("true"),"Boolean.TYPE");
        assertEquals(Character.valueOf('B'), beanMap.getTypeTransformer(Character.TYPE).apply("BCD"),"Character.TYPE");
        assertEquals(Byte.valueOf((byte) 1), beanMap.getTypeTransformer(Byte.TYPE).apply("1"),"Byte.TYPE");
        assertEquals(Short.valueOf((short) 2), beanMap.getTypeTransformer(Short.TYPE).apply("2"),"Short.TYPE");
        assertEquals(Integer.valueOf(3), beanMap.getTypeTransformer(Integer.TYPE).apply("3"),"Integer.TYPE");
        assertEquals(Long.valueOf(4), beanMap.getTypeTransformer(Long.TYPE).apply("4"),"Long.TYPE");
        assertEquals(Float.valueOf("5"), beanMap.getTypeTransformer(Float.TYPE).apply("5"),"Float.TYPE");
        assertEquals(Double.valueOf("6"), beanMap.getTypeTransformer(Double.TYPE).apply("6"),"Double.TYPE");
    }

    /**
     * Need to override this method because the "clear()" method on the bean
     * map just returns the bean properties to their default states.  It does
     * not actually remove the mappings as per the map contract.  The default
     * testClear() methods checks that the clear method throws an
     * UnsupportedOperationException since this class is not add/remove
     * modifiable.  In our case though, we do not always throw that exception.
     */
    @Override
    public void testMapClear() {
        //TODO: make sure a call to BeanMap.clear returns the bean to its
        //default initialization values.
    }

    /**
     * Need to override this method because the "put()" method on the bean
     * doesn't work for this type of Map.
     */
    @Override
    public void testMapPut() {
        // see testBeanMapPutAllWriteable
    }

    public void testMethodAccessor() throws Exception {
        final BeanMap map = (BeanMap) makeFullMap();
        final Method method = BeanWithProperties.class.getDeclaredMethod("getSomeIntegerValue");
        assertEquals(method, map.getReadMethod("someIntegerValue"));
    }

    public void testMethodMutator() throws Exception {
        final BeanMap map = (BeanMap) makeFullMap();
        final Method method = BeanWithProperties.class.getDeclaredMethod("setSomeIntegerValue", Integer.class);
        assertEquals(method, map.getWriteMethod("someIntegerValue"));
    }

    /**
     * Values is a dead copy in BeanMap, so refresh each time.
     */
    @Override
    public void verifyValues() {
        values = map.values();
        super.verifyValues();
    }
}
