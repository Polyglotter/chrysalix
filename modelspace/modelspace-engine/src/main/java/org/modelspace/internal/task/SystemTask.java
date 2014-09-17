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
package org.modelspace.internal.task;

import javax.jcr.Node;
import javax.jcr.Session;

import org.modelspace.internal.ModelspaceImpl;

/**
 * 
 */
public interface SystemTask {

    /**
     * @param session
     *        a new session
     * @param systemNode
     *        the node containing the system properties for the system class within which this task is being
     *        {@link ModelspaceImpl#run(Object, SystemTask) run}.
     * @throws Exception
     *         if any problem occurs
     */
    void run( Session session,
              Node systemNode ) throws Exception;
}
