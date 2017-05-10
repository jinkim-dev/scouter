// Generated by delombok at Sun Feb 26 12:31:38 KST 2017
package scouter.bytebuddy.implementation;


import scouter.bytebuddy.utility.privilege.SetAccessibleAction;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementations of this interface explicitly initialize a loaded type. Usually, such implementations inject runtime
 * context into an instrumented type which cannot be defined by the means of the Java class file format.
 */
public interface LoadedTypeInitializer {
    /**
     * Callback that is invoked on the creation of an instrumented type. If the loaded type initializer is alive, this
     * method should be implemented empty instead of throwing an exception.
     *
     * @param type The manifestation of the instrumented type.
     */
    void onLoad(Class<?> type);

    /**
     * Indicates if this initializer is alive and needs to be invoked. This is only meant as a mark. A loaded type
     * initializer that is not alive might still be called and must therefore not throw an exception but rather
     * provide an empty implementation.
     *
     * @return {@code true} if this initializer is alive.
     */
    boolean isAlive();


    /**
     * A loaded type initializer that does not do anything.
     */
    enum NoOp implements LoadedTypeInitializer {
        /**
         * The singleton instance.
         */
        INSTANCE;

        @Override
        public void onLoad(Class<?> type) {
            /* do nothing */
        }

        @Override
        public boolean isAlive() {
            return false;
        }
    }


    /**
     * A type initializer for setting a value for a static field.
     */
    class ForStaticField implements LoadedTypeInitializer, Serializable {
        /**
         * This class's serial version UID.
         */
        private static final long serialVersionUID = 1L;
        /**
         * A value for accessing a static field.
         */
        private static final Object STATIC_FIELD = null;
        /**
         * The name of the field.
         */
        private final String fieldName;
        /**
         * The value of the field.
         */
        private final Object value;

        /**
         * Creates a new {@link LoadedTypeInitializer} for setting a static field.
         *
         * @param fieldName the name of the field.
         * @param value     The value to be set.
         */
        protected ForStaticField(String fieldName, Object value) {
            this.fieldName = fieldName;
            this.value = value;
        }

        @Override
        public void onLoad(Class<?> type) {
            try {
                Field field = type.getDeclaredField(fieldName);
                if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
                    AccessController.doPrivileged(new SetAccessibleAction<Field>(field));
                }
                field.set(STATIC_FIELD, value);
            } catch (IllegalAccessException exception) {
                throw new IllegalArgumentException("Cannot access " + fieldName + " from " + type, exception);
            } catch (NoSuchFieldException exception) {
                throw new IllegalStateException("There is no field " + fieldName + " defined on " + type, exception);
            }
        }

        @Override
        public boolean isAlive() {
            return true;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @javax.annotation.Generated("lombok")
        public boolean equals(final java.lang.Object o) {
            if (o == this) return true;
            if (!(o instanceof LoadedTypeInitializer.ForStaticField)) return false;
            final LoadedTypeInitializer.ForStaticField other = (LoadedTypeInitializer.ForStaticField) o;
            if (!other.canEqual((java.lang.Object) this)) return false;
            final java.lang.Object this$fieldName = this.fieldName;
            final java.lang.Object other$fieldName = other.fieldName;
            if (this$fieldName == null ? other$fieldName != null : !this$fieldName.equals(other$fieldName)) return false;
            final java.lang.Object this$value = this.value;
            final java.lang.Object other$value = other.value;
            if (this$value == null ? other$value != null : !this$value.equals(other$value)) return false;
            return true;
        }

        @java.lang.SuppressWarnings("all")
        @javax.annotation.Generated("lombok")
        protected boolean canEqual(final java.lang.Object other) {
            return other instanceof LoadedTypeInitializer.ForStaticField;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @javax.annotation.Generated("lombok")
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final java.lang.Object $fieldName = this.fieldName;
            result = result * PRIME + ($fieldName == null ? 43 : $fieldName.hashCode());
            final java.lang.Object $value = this.value;
            result = result * PRIME + ($value == null ? 43 : $value.hashCode());
            return result;
        }
    }


    /**
     * A compound loaded type initializer that combines several type initializers.
     */

    class Compound implements LoadedTypeInitializer, Serializable {
        /**
         * This class's serial version UID.
         */
        private static final long serialVersionUID = 1L;
        /**
         * The loaded type initializers that are represented by this compound type initializer.
         */
        private final List<LoadedTypeInitializer> loadedTypeInitializers;

        /**
         * Creates a new compound loaded type initializer.
         *
         * @param loadedTypeInitializer A number of loaded type initializers in their invocation order.
         */
        public Compound(LoadedTypeInitializer... loadedTypeInitializer) {
            this(Arrays.asList(loadedTypeInitializer));
        }

        /**
         * Creates a new compound loaded type initializer.
         *
         * @param loadedTypeInitializers A number of loaded type initializers in their invocation order.
         */
        public Compound(List<? extends LoadedTypeInitializer> loadedTypeInitializers) {
            this.loadedTypeInitializers = new ArrayList<LoadedTypeInitializer>();
            for (LoadedTypeInitializer loadedTypeInitializer : loadedTypeInitializers) {
                if (loadedTypeInitializer instanceof Compound) {
                    this.loadedTypeInitializers.addAll(((Compound) loadedTypeInitializer).loadedTypeInitializers);
                } else if (!(loadedTypeInitializer instanceof NoOp)) {
                    this.loadedTypeInitializers.add(loadedTypeInitializer);
                }
            }
        }

        @Override
        public void onLoad(Class<?> type) {
            for (LoadedTypeInitializer loadedTypeInitializer : loadedTypeInitializers) {
                loadedTypeInitializer.onLoad(type);
            }
        }

        @Override
        public boolean isAlive() {
            for (LoadedTypeInitializer loadedTypeInitializer : loadedTypeInitializers) {
                if (loadedTypeInitializer.isAlive()) {
                    return true;
                }
            }
            return false;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @javax.annotation.Generated("lombok")
        public boolean equals(final java.lang.Object o) {
            if (o == this) return true;
            if (!(o instanceof LoadedTypeInitializer.Compound)) return false;
            final LoadedTypeInitializer.Compound other = (LoadedTypeInitializer.Compound) o;
            if (!other.canEqual((java.lang.Object) this)) return false;
            final java.lang.Object this$loadedTypeInitializers = this.loadedTypeInitializers;
            final java.lang.Object other$loadedTypeInitializers = other.loadedTypeInitializers;
            if (this$loadedTypeInitializers == null ? other$loadedTypeInitializers != null : !this$loadedTypeInitializers.equals(other$loadedTypeInitializers)) return false;
            return true;
        }

        @java.lang.SuppressWarnings("all")
        @javax.annotation.Generated("lombok")
        protected boolean canEqual(final java.lang.Object other) {
            return other instanceof LoadedTypeInitializer.Compound;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @javax.annotation.Generated("lombok")
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final java.lang.Object $loadedTypeInitializers = this.loadedTypeInitializers;
            result = result * PRIME + ($loadedTypeInitializers == null ? 43 : $loadedTypeInitializers.hashCode());
            return result;
        }
    }
}