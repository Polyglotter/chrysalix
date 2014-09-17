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
package org.modelspace;

/**
 * The lexicon of the modelspace.
 */
public interface ModelspaceLexicon {

    /**
     * The Modelspace namespace prefix. Value is {@value} .
     */
    String NAMESPACE_PREFIX = "mm";

    /**
     * The node type name whose children are all metamodel categories.
     */
    String METAMODEL_CATEGORIES = NAMESPACE_PREFIX + ":metamodelCategories";

    /**
     * the system workspace property containing the registered metamodel repositories
     */
    String METAMODEL_REPOSITORIES = NAMESPACE_PREFIX + ":metamodelRepositories";

    /**
     * Temporary workspace folder
     */
    String TEMP_FOLDER = NAMESPACE_PREFIX + ":temp";

    /**
     * The mixin for the node where model nodes can be added to.
     */
    String UNSTRUCTURED_MIXIN = NAMESPACE_PREFIX + ":unstructured";

    /**
     * JCR names related to a model dependency.
     */
    interface Dependency {

        /**
         * The node type name of a dependency node.
         */
        String DEPENDENCY = NAMESPACE_PREFIX + ":dependency";

        /**
         * The name of a dependency node's derived path property.
         */
        String PATH = NAMESPACE_PREFIX + ":path";

        /**
         * The name of a dependency node's source reference property.
         */
        String SOURCE_REFERENCE_PROPERTY = NAMESPACE_PREFIX + ":sourceReference";

    }

    /**
     * JCR names related to a metamodel.
     */
    interface Metamodel {

        /**
         * The node type name of a category.
         */
        String NODE_TYPE = NAMESPACE_PREFIX + ":metamodel";

        /**
         * The name of the property whose value is the class name of the metamodel's importer.
         */
        String IMPORTER_CLASS_NAME = NAMESPACE_PREFIX + ":importerClassName";

        /**
         * The name of the property whose value is the class name of the metamodel's exporter.
         */
        String EXPORTER_CLASS_NAME = NAMESPACE_PREFIX + ":exporterClassName";

        /**
         * The name of the property whose value is the class name of the metamodel's dependency processor.
         */
        String DEPENDENCY_PROCESSOR_CLASS_NAME = NAMESPACE_PREFIX + ":dependencyProcessorClassName";

        /**
         * JCR names related to a metamodel category.
         */
        interface Category {

            /**
             * The node type name of a category.
             */
            String NODE_TYPE = NAMESPACE_PREFIX + ":metamodelCategory";

            /**
             * The node type name and type whose children are all archives.
             */
            String ARCHIVES = NAMESPACE_PREFIX + ":archives";

            /**
             * The node type name and type whose children are all metamodels.
             */
            String METAMODELS = NAMESPACE_PREFIX + ":metamodels";

        }

        /**
         * JCR names related to a metamodel extension.
         */
        interface Extension {

            /**
             * The node type name of the abstract node type definition for a metamodel extension.
             */
            String NODE_TYPE = NAMESPACE_PREFIX + ":metamodelExtension";

            /**
             * The property name whose value is the an extension type.
             */

            String EXTENSION_TYPE = NAMESPACE_PREFIX + ":extensionType";

            /**
             * The property name whose value is the class name of the metamodel extension.
             */
            String CLASS_NAME = NAMESPACE_PREFIX + ":className";

            /**
             * The property name whose value is the reference to the archive that was used to create the metamodel or load a
             * metamodel extension.
             */
            String ARCHIVE_REFERENCE = NAMESPACE_PREFIX + ":archiveReference";

            /**
             * The name of the dependency processor node type definition.
             */
            String DEPENDENCY_PROCESSOR_NODE_TYPE = NAMESPACE_PREFIX + ":dependencyProcessor";

            /**
             * The name of the exporter node type definition.
             */
            String EXPORTER_NODE_TYPE = NAMESPACE_PREFIX + ":exporter";

            /**
             * The name of the importer node type definition.
             */
            String IMPORTER_NODE_TYPE = NAMESPACE_PREFIX + ":importer";

            /**
             * The name of the multi-valued property whose values are regular expressions used to mark file names as potential
             * extensions of this type.
             */
            String NAME_PATTERNS = NAMESPACE_PREFIX + ":namePatterns";
        }
    }

    /**
     * JCR names related to a model.
     */
    interface Model {

        /**
         * The node type name of the dependencies node. This node will have dependency child nodes.
         */
        String DEPENDENCIES = NAMESPACE_PREFIX + ":dependencies";

        /**
         * The name of the external location property the imported resource was sourced from.
         */
        String EXTERNAL_LOCATION = NAMESPACE_PREFIX + ":externalLocation";

        /**
         * The mixin type of a model node.
         */
        String MODEL_MIXIN = NAMESPACE_PREFIX + ":model";

        /**
         * The name of the property whose value is a reference to the model's metamodel.
         */
        String METAMODEL = NAMESPACE_PREFIX + ":metamodel";

    }

}
