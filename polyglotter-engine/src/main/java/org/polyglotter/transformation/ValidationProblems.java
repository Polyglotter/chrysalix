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

import java.util.List;

import org.polyglotter.transformation.ValidationProblem.Severity;

/**
 * A collection of {@link ValidationProblem validation problems}.
 */
public interface ValidationProblems extends List< ValidationProblem > {

    /**
     * @return <code>true</code> if the collection contains a problem that has an {@link Severity#ERROR error} severity
     */
    boolean isError();

    /**
     * @return <code>true</code> if the collection contains a problem that has an {@link Severity#INFO info} severity and does not
     *         contain problems with {@link Severity#ERROR error} or {@link Severity#WARNING warning} severity
     */
    boolean isInfo();

    /**
     * @return <code>true</code> if all the problems in the collection have an {@link Severity#OK OK} severity
     */
    boolean isOk();

    /**
     * @return <code>true</code> if the collection contains a problem that has a {@link Severity#WARNING warning} severity and does
     *         not contain a problem with an {@link Severity#ERROR error} severity
     */
    boolean isWarning();

}
