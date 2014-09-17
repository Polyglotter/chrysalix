/*
 * Chrysalix
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * See the AUTHORS.txt file in the distribution for a full listing of 
 * individual contributors.
 *
 * Chrysalix is free software. Unless otherwise indicated, all code in Chrysalix
 * is licensed to you under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * Chrysalix is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.chrysalix.transformation;

import org.chrysalix.common.CheckArg;

/**
 * A problem created when validating a {@link Transformation transformation}-related object like an {@link Operation operation}.
 */
public interface ValidationProblem {

    /**
     * @return <code>true</code> if the problem has an {@link Severity#ERROR error} severity
     */
    boolean isError();

    /**
     * @return <code>true</code> if the problem has an {@link Severity#INFO info} severity
     */
    boolean isInfo();

    /**
     * @return <code>true</code> if the problem has an {@link Severity#OK OK} severity
     */
    boolean isOk();

    /**
     * @return <code>true</code> if the problem has a {@link Severity#WARNING warning} severity
     */
    boolean isWarning();

    /**
     * @return the problem message (can be <code>null</code> or empty)
     */
    String message();

    /**
     * @return the problem severity (never <code>null</code>)
     */
    Severity severity();

    /**
     * @return the identifier of the transformation that was validated (never <code>null</code> or empty)
     */
    String sourceId();

    /**
     * The message severity.
     */
    enum Severity {
        ERROR,
        WARNING,
        INFO,
        OK;

        /**
         * @param that
         *        the severity being compared to (cannot be <code>null</code>)
         * @return <code>true</code> if severity is more severe
         */
        public boolean isMoreSevereThan( final Severity that ) {
            CheckArg.notNull( that, "that" );

            if ( this == that ) {
                return false;
            }

            if ( this == ERROR ) {
                return ( that != ERROR );
            }

            if ( this == WARNING ) {
                return ( ( that == ERROR ) ? false : ( that != WARNING ) );
            }

            if ( this == INFO ) {
                return ( ( that == OK ) ? true : false );
            }

            // this == OK
            return false;
        }
    }

}
