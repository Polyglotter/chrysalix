/*
 * Polyglotter (http://polyglotter.org)
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * See the AUTHORS.txt file in the distribution for a full listing of 
 * individual contributors.
 *
 * Polyglotter is free software. Unless otherwise indicated, all code in Polyglotter
 * is licensed to you under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * Polyglotter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.polyglotter.transformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.xml.namespace.QName;

import org.polyglotter.Polyglotter;
import org.polyglotter.common.CheckArg;
import org.polyglotter.common.PolyglotterException;
import org.polyglotter.internal.NumberValue;
import org.polyglotter.internal.StringValue;
import org.polyglotter.operation.ValueImpl;
import org.polyglotter.transformation.TransformationEvent.EventType;
import org.polyglotter.transformation.ValidationProblem.Severity;

/**
 * A factory for creating {@link Transformation transformation}-related objects.
 */
public final class TransformationFactory {

    /**
     * @param transformationId
     *        the source transformation's identifier (cannot be <code>null</code>)
     * @param message
     *        the problem message (can be <code>null</code> or empty)
     * @return the problem (never <code>null</code>)
     * @throws IllegalArgumentException
     *         if the transformation identifier is <code>null</code>
     */
    public static ValidationProblem createError( final QName transformationId,
                                                 final String message ) {
        CheckArg.notNull( transformationId, "transformationId" );
        return new Problem( Severity.ERROR, transformationId, message );
    }

    /**
     * @param type
     *        the event type (cannot be <code>null</code>)
     * @param source
     *        the value that sourced this event (cannot be <code>null</code>)
     * @param data
     *        an optional collection of event data
     * @return the event (never <code>null</code>)
     * @throws IllegalArgumentException
     *         if the type or source is <code>null</code>
     */
    public static TransformationEvent createEvent( final EventType type,
                                                   final Value< ? > source,
                                                   final Map< String, Object > data ) {
        CheckArg.notNull( type, "type" );
        CheckArg.notNull( source, "source" );
        return new Event( type, source, data );
    }

    /**
     * @param type
     *        the event type (cannot be <code>null</code>)
     * @param source
     *        the value that sourced this event (cannot be <code>null</code>)
     * @param key
     *        the identifier of event data (cannot be <code>null</code> or empty)
     * @param value
     *        the value of event data (can be <code>null</code>)
     * @return the event (never <code>null</code>)
     * @throws IllegalArgumentException
     *         if the type, source, or key is <code>null</code>
     */
    public static TransformationEvent createEvent( final EventType type,
                                                   final Value< ? > source,
                                                   final String key,
                                                   final Object value ) {
        CheckArg.notNull( key, "key" );
        return createEvent( type, source, Collections.singletonMap( key, value ) );
    }

    /**
     * Creates an ID using the {@link Polyglotter#NAMESPACE_PREFIX} and {@link Polyglotter#NAMESPACE_URI}.
     * 
     * @param name
     *        the identifier's local part name (cannot be <code>null</code> or empty)
     * @return the ID (never <code>null</code>)
     */
    public static QName createId( final String name ) {
        CheckArg.notEmpty( name, "name" );
        return createId( Polyglotter.NAMESPACE_PREFIX, name, Polyglotter.NAMESPACE_URI );
    }

    /**
     * @param namespacePrefix
     *        the identifier's namespace prefix (can be <code>null</code> or empty)
     * @param namespaceUri
     *        the identifier's namespace URI (can be <code>null</code> or empty)
     * @param name
     *        the identifier's local part name (cannot be <code>null</code> or empty)
     * @return the ID (never <code>null</code>)
     * @throws IllegalArgumentException
     *         if the name is <code>null</code> or empty
     */
    public static QName createId( final String namespacePrefix,
                                  final String namespaceUri,
                                  final String name ) {
        CheckArg.notEmpty( name, "name" );
        return new QName( namespaceUri, name, namespacePrefix );
    }

    /**
     * @param transformationId
     *        the source transformation's identifier (cannot be <code>null</code>)
     * @param message
     *        the problem message (can be <code>null</code> or empty)
     * @return the problem (never <code>null</code>)
     * @throws IllegalArgumentException
     *         if the transformation identifier is <code>null</code>
     */
    public static ValidationProblem createInfo( final QName transformationId,
                                                final String message ) {
        CheckArg.notNull( transformationId, "transformationId" );
        return new Problem( Severity.INFO, transformationId, message );
    }

    /**
     * @param descriptor
     *        the value descriptor (cannot be <code>null</code>)
     * @param initialValue
     *        the initial value (can be <code>null</code>)
     * @return the new number value (never <code>null</code>)
     * @throws PolyglotterException
     *         if there is a problem setting the initial value
     */
    public static Value< Number > createNumberValue( final ValueDescriptor< Number > descriptor,
                                                     final Number initialValue ) throws PolyglotterException {
        return new NumberValue( descriptor, initialValue );
    }

    /**
     * @param transformationId
     *        the source transformation's identifier (cannot be <code>null</code>)
     * @param message
     *        the problem message (can be <code>null</code> or empty)
     * @return the problem (never <code>null</code>)
     * @throws IllegalArgumentException
     *         if the transformation identifier is <code>null</code>
     */
    public static ValidationProblem createOk( final QName transformationId,
                                              final String message ) {
        CheckArg.notNull( transformationId, "transformationId" );
        return new Problem( Severity.OK, transformationId, message );
    }

    /**
     * Creates a descriptor that is not modifiable, not unbounded, and requires only one value.
     * 
     * @param valueId
     *        the value identifier (cannot be <code>null</code>)
     * @param valueDescription
     *        the value description (cannot be <code>null</code> or empty)
     * @param valueName
     *        the value name (cannot be <code>null</code> or empty)
     * @param valueType
     *        the value type (cannot be <code>null</code>)
     * @return the value descriptor (never <code>null</code>)
     */
    public static < T extends Object > ValueDescriptor< T > createReadOnlyBoundedOneValueDescriptor( final QName valueId,
                                                                                                     final String valueDescription,
                                                                                                     final String valueName,
                                                                                                     final Class< T > valueType ) {
        return createValueDescriptor( valueId, valueDescription, valueName, valueType, false, 1, false );
    }

    /**
     * @param descriptor
     *        the value descriptor (cannot be <code>null</code>)
     * @param initialValue
     *        the initial value (can be <code>null</code> or empty)
     * @return the new string value (never <code>null</code>)
     * @throws PolyglotterException
     *         if there is a problem setting the initial value
     */
    public static Value< String > createStringValue( final ValueDescriptor< String > descriptor,
                                                     final String initialValue ) throws PolyglotterException {
        return new StringValue( descriptor, initialValue );
    }

    /**
     * @return an empty validation problems collection (never <code>null</code>)
     */
    public static ValidationProblems createValidationProblems() {
        return new Problems();
    }

    /**
     * @param descriptor
     *        the value descriptor (cannot be <code>null</code>)
     * @return the new value (never <code>null</code>)
     */
    public static < T extends Object > Value< T > createValue( final ValueDescriptor< T > descriptor ) {
        CheckArg.notNull( descriptor, "descriptor" );
        return new ValueImpl< T >( descriptor );
    }

    /**
     * @param descriptor
     *        the value descriptor (cannot be <code>null</code>)
     * @param value
     *        the initial value (can be <code>null</code>)
     * @return the new value (never <code>null</code>)
     * @throws PolyglotterException
     *         if an error occurs
     * @throws UnsupportedOperationException
     *         if the descriptor is read-only
     */
    public static < T extends Object > Value< T > createValue( final ValueDescriptor< T > descriptor,
                                                               final T value ) throws PolyglotterException {
        final Value< T > result = createValue( descriptor );
        result.set( value );

        return result;
    }

    /**
     * @param valueId
     *        the value identifier (cannot be <code>null</code>)
     * @param valueDescription
     *        the value description (cannot be <code>null</code> or empty)
     * @param valueName
     *        the value name (cannot be <code>null</code> or empty)
     * @param valueType
     *        the value type (cannot be <code>null</code>)
     * @param isModifiable
     *        <code>true</code> if value is modifiable
     * @param requiredValueCount
     *        the number of required values (cannot be a negative number)
     * @param isUnbounded
     *        <code>true</code> if there is no limit to the number of values
     * @return the value descriptor (never <code>null</code>)
     */
    public static < T extends Object > ValueDescriptor< T > createValueDescriptor( final QName valueId,
                                                                                   final String valueDescription,
                                                                                   final String valueName,
                                                                                   final Class< T > valueType,
                                                                                   final boolean isModifiable,
                                                                                   final int requiredValueCount,
                                                                                   final boolean isUnbounded ) {
        return new ValueDescriptorImpl< T >( valueId, valueDescription, valueName, valueType, isModifiable, requiredValueCount, isUnbounded );
    }

    /**
     * @param transformationId
     *        the source transformation's identifier (cannot be <code>null</code>)
     * @param message
     *        the problem message (can be <code>null</code> or empty)
     * @return the problem (never <code>null</code>)
     * @throws IllegalArgumentException
     *         if the transformation identifier is <code>null</code>
     */
    public static ValidationProblem createWarning( final QName transformationId,
                                                   final String message ) {
        CheckArg.notNull( transformationId, "transformationId" );
        return new Problem( Severity.WARNING, transformationId, message );
    }

    /**
     * Creates a descriptor that is modifiable, not unbounded, and requires only one value.
     * 
     * @param valueId
     *        the value identifier (cannot be <code>null</code>)
     * @param valueDescription
     *        the value description (cannot be <code>null</code> or empty)
     * @param valueName
     *        the value name (cannot be <code>null</code> or empty)
     * @param valueType
     *        the value type (cannot be <code>null</code>)
     * @return the value descriptor (never <code>null</code>)
     */
    public static < T extends Object > ValueDescriptor< T > createWritableBoundedOneValueDescriptor( final QName valueId,
                                                                                                     final String valueDescription,
                                                                                                     final String valueName,
                                                                                                     final Class< T > valueType ) {
        return createValueDescriptor( valueId, valueDescription, valueName, valueType, true, 1, false );
    }

    /**
     * Don't allow construction outside this class.
     */
    private TransformationFactory() {
        // nothing to do
    }

    private static class Event implements TransformationEvent {

        private final Map< String, Object > data;
        private final Object source;
        private final EventType type;

        Event( final EventType eventType,
               final Object eventSource,
               final Map< String, Object > eventData ) {
            this.type = eventType;
            this.source = eventSource;
            this.data = eventData;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.TransformationEvent#data()
         */
        @Override
        public Map< String, ? > data() {
            return this.data;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.TransformationEvent#source()
         */
        @Override
        public Object source() {
            return this.source;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return ( "event type = " + this.type + ", source = " + this.source.getClass().getSimpleName() + ", data = " + this.data );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.TransformationEvent#type()
         */
        @Override
        public EventType type() {
            return this.type;
        }

    }

    private static final class Problem implements ValidationProblem {

        private final Severity severity;
        private final String message;
        private final QName sourceId;

        Problem( final Severity problemSeverity,
                 final QName problemPartId,
                 final String problemMessage ) {
            this.severity = problemSeverity;
            this.message = problemMessage;
            this.sourceId = problemPartId;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValidationProblem#isError()
         */
        @Override
        public boolean isError() {
            return ( this.severity == Severity.ERROR );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValidationProblem#isInfo()
         */
        @Override
        public boolean isInfo() {
            return ( this.severity == Severity.INFO );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValidationProblem#isOk()
         */
        @Override
        public boolean isOk() {
            return ( this.severity == Severity.OK );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValidationProblem#isWarning()
         */
        @Override
        public boolean isWarning() {
            return ( this.severity == Severity.WARNING );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValidationProblem#message()
         */
        @Override
        public String message() {
            return this.message;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValidationProblem#severity()
         */
        @Override
        public Severity severity() {
            return this.severity;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValidationProblem#sourceId()
         */
        @Override
        public QName sourceId() {
            return this.sourceId;
        }

    }

    private static final class Problems extends ArrayList< ValidationProblem > implements ValidationProblems {

        private Severity severity;

        Problems() {
            this.severity = Severity.OK;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#add(int, java.lang.Object)
         */
        @Override
        public void add( final int index,
                         final ValidationProblem problem ) {
            super.add( index, problem );
            updateSeverity( problem );
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#add(java.lang.Object)
         */
        @Override
        public boolean add( final ValidationProblem problem ) {
            final boolean added = super.add( problem );

            if ( added ) {
                updateSeverity( problem );
            }

            return added;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#addAll(java.util.Collection)
         */
        @Override
        public boolean addAll( final Collection< ? extends ValidationProblem > c ) {
            if ( this.severity != Severity.ERROR ) {
                for ( final ValidationProblem problem : c ) {
                    updateSeverity( problem );

                    if ( this.severity == Severity.ERROR ) {
                        break;
                    }
                }
            }

            return super.addAll( c );
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#addAll(int, java.util.Collection)
         */
        @Override
        public boolean addAll( final int index,
                               final Collection< ? extends ValidationProblem > c ) {
            if ( this.severity != Severity.ERROR ) {
                for ( final ValidationProblem problem : c ) {
                    updateSeverity( problem );

                    if ( this.severity == Severity.ERROR ) {
                        break;
                    }
                }
            }

            return super.addAll( index, c );
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#clear()
         */
        @Override
        public void clear() {
            super.clear();
            this.severity = Severity.OK;
        }

        private void determineSeverity() {
            this.severity = Severity.OK;

            for ( final ValidationProblem problem : this ) {
                updateSeverity( problem );

                if ( this.severity == Severity.ERROR ) {
                    break;
                }
            }
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValidationProblems#isError()
         */
        @Override
        public boolean isError() {
            return ( this.severity == Severity.ERROR );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValidationProblems#isInfo()
         */
        @Override
        public boolean isInfo() {
            return ( this.severity == Severity.INFO );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValidationProblems#isOk()
         */
        @Override
        public boolean isOk() {
            return ( this.severity == Severity.OK );
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValidationProblems#isWarning()
         */
        @Override
        public boolean isWarning() {
            return ( this.severity == Severity.WARNING );
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#remove(int)
         */
        @Override
        public ValidationProblem remove( final int index ) {
            final ValidationProblem problem = super.remove( index );

            if ( this.severity == problem.severity() ) {
                determineSeverity();
            }

            return problem;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#remove(java.lang.Object)
         */
        @Override
        public boolean remove( final Object o ) {
            final boolean removed = super.remove( o );

            if ( removed ) {
                final ValidationProblem problem = ( ValidationProblem ) o;

                if ( problem.severity() == this.severity ) {
                    determineSeverity();
                }
            }

            return removed;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#removeAll(java.util.Collection)
         */
        @Override
        public boolean removeAll( final Collection< ? > c ) {
            final boolean changed = super.removeAll( c );

            if ( changed ) {
                determineSeverity();
            }

            return changed;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#retainAll(java.util.Collection)
         */
        @Override
        public boolean retainAll( final Collection< ? > c ) {
            final boolean changed = super.retainAll( c );

            if ( changed ) {
                determineSeverity();
            }

            return changed;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.ArrayList#set(int, java.lang.Object)
         */
        @Override
        public ValidationProblem set( final int index,
                                      final ValidationProblem newProblem ) {
            final ValidationProblem oldProblem = super.set( index, newProblem );

            if ( newProblem.severity().isMoreSevereThan( this.severity ) ) {
                this.severity = newProblem.severity();
            } else if ( oldProblem.severity() == this.severity ) {
                determineSeverity();
            }

            return oldProblem;
        }

        private void updateSeverity( final ValidationProblem problem ) {
            if ( problem.severity().isMoreSevereThan( this.severity ) ) {
                this.severity = problem.severity();
            }
        }

    }

    private static class ValueDescriptorImpl< T > implements ValueDescriptor< T > {

        private final String description;
        private final QName id;
        private final boolean modifiable;
        private final String name;
        private final int numRequiredValues;
        private final Class< T > type;
        private final boolean unbounded;

        /**
         * @param valueId
         *        the value identifier (cannot be <code>null</code>)
         * @param valueDescription
         *        the value description (cannot be <code>null</code> or empty)
         * @param valueName
         *        the value name (cannot be <code>null</code> or empty)
         * @param valueType
         *        the value type (cannot be <code>null</code>)
         * @param isModifiable
         *        <code>true</code> if value is modifiable
         * @param requiredValueCount
         *        the number of required values (cannot be a negative number)
         * @param isUnbounded
         *        <code>true</code> if there is no limit to the number of values
         */
        ValueDescriptorImpl( final QName valueId,
                             final String valueDescription,
                             final String valueName,
                             final Class< T > valueType,
                             final boolean isModifiable,
                             final int requiredValueCount,
                             final boolean isUnbounded ) {
            CheckArg.notNull( valueId, "valueId" );
            CheckArg.notEmpty( valueDescription, "valueDescription" );
            CheckArg.notEmpty( valueName, "valueName" );
            CheckArg.notNull( valueType, "valueType" );
            CheckArg.isNonNegative( requiredValueCount, "requiredValueCount" );

            this.id = valueId;
            this.description = valueDescription;
            this.name = valueName;
            this.type = valueType;
            this.modifiable = isModifiable;
            this.numRequiredValues = requiredValueCount;
            this.unbounded = isUnbounded;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValueDescriptor#description()
         */
        @Override
        public String description() {
            return this.description;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValueDescriptor#id()
         */
        @Override
        public QName id() {
            return this.id;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValueDescriptor#modifiable()
         */
        @Override
        public boolean modifiable() {
            return this.modifiable;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValueDescriptor#name()
         */
        @Override
        public String name() {
            return this.name;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValueDescriptor#namespace()
         */
        @Override
        public String namespace() {
            return this.id.getNamespaceURI();
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValueDescriptor#requiredValueCount()
         */
        @Override
        public int requiredValueCount() {
            return this.numRequiredValues;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValueDescriptor#type()
         */
        @Override
        public Class< T > type() {
            return this.type;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.polyglotter.transformation.ValueDescriptor#unbounded()
         */
        @Override
        public boolean unbounded() {
            return this.unbounded;
        }

    }

}
