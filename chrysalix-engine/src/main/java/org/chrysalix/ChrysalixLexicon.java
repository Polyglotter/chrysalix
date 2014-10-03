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
package org.chrysalix;

import org.chrysalix.transformation.ValueDescriptor;

/**
 * Defines constants that represent namespaces, node types, and node properties related to the Polyglotter data mapper and defined
 * in one or more CND files.
 */
public interface ChrysalixLexicon {

    /**
     * JCR names related to Polyglotter operation inputs. Nodes are named using the input's {@link ValueDescriptor descriptor}
     * identifier.
     */
    interface Input {

        /**
         * The JCR node type name for an input node.
         */
        String NODE_TYPE = Namespace.PREFIX + ":input";

        /**
         * The name of the <code>path</code> boolean property. The value will be <code>true</code> if the {@link #VALUE value}
         * property represents a node path.
         */
        String PATH = Namespace.PREFIX + ":path";

        /**
         * The name of the <code>value</code> string property. Normally this value represents a model object path but it can also be
         * a literal value.
         */
        String VALUE = Namespace.PREFIX + ":value";

    }

    /**
     * Polyglotter namespace related constants.
     */
    interface Namespace {

        /**
         * The Polyglotter data mapper namespace prefix.
         */
        String PREFIX = "tx";

        /**
         * The Polglotter data mapper URI.
         */
        String URI = "http://www.polyglotter.org/transform/1.0";

    }

    /**
     * JCR names related to Polyglotter operations. Nodes are named using the operation's {@link ValueDescriptor output descriptor}
     * identifier. Child nodes for the operation inputs are named using the input's descriptor ID.
     */
    interface Operation {

        /**
         * The JCR node type name for an operation node.
         */
        String NODE_TYPE = Namespace.PREFIX + ":operation";

    }

    /**
     * JCR names related to Polyglotter transformations. Child nodes for the transformation operations are named using the
     * operation's output descriptor IDs.
     */
    interface Transformation {

        /**
         * The JCR node type name for a transformation node.
         */
        String NODE_TYPE = Namespace.PREFIX + ":transformation";

        /**
         * The name of the <code>sources</code> multi-valued property. The values consist of model paths.
         */
        String SOURCES = Namespace.PREFIX + ":sources";

        /**
         * The name of the <code>targets</code> multi-valued property. The values consist of model paths.
         */
        String TARGETS = Namespace.PREFIX + ":targets";

    }

}
