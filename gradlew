#!/usr/bin/env sh

#
# Copyright 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#
#@author: Andres Almiray
#

#
# Sdkman custom initialization script.
#
# This script is sourced by the main sdkman script, to allow custom behavior.
# For example, you could use it to specify a custom service endpoint.
#
# SDKMAN_CANDIDATES_API="https://some.host.com/candidates"
#

#
# SDKMAN_DIR is defined in the main sdkman script.
#
# It is possible to override the location of the sdkman home directory.
#
# export SDKMAN_DIR="/usr/local/sdkman"
#

#
# SDKMAN_PLATFORM is defined in the main sdkman script.
#
# It is possible to override the platform.
#
# export SDKMAN_PLATFORM="linux-x64"
#

#
# By default, sdkman will search for a local configuration file in the current working directory.
# This file is named ".sdkmanrc" and can be used to override the default candidate version.
# For example, if you want to use a specific version of groovy, you can create a file named
# ".sdkmanrc" with the following content:
#
# groovy=2.4.21
#
# It is possible to change the name of this file.
#
# SDKMAN_RC_FILENAME=".custom_sdkmanrc"
#

#
# By default, sdkman will not perform any version validation, allowing the installation of any
# version of any candidate. If you want to validate the version, you can set the following
# variable to "true".
#
# SDKMAN_VERSION_VALIDATION="true"
#

#
# By default, sdkman will use colors to decorate output. If you want to disable colors, you can
# set the following variable to "false".
#
# SDKMAN_USE_COLORS="false"
#

#
# By default, sdkman will use curl to download candidates. If you want to use a different
# command, you can set the following variable.
#
# SDKMAN_CURL_COMMAND="curl -L"
#

#
# By default, sdkman will use unzip to extract candidates. If you want to use a different
# command, you can set the following variable.
#
# SDKMAN_UNZIP_COMMAND="unzip"
#

#
# By default, sdkman will use zip to create candidates. If you want to use a different
# command, you can set the following variable.
#
# SDKMAN_ZIP_COMMAND="zip"
#

#
# By default, sdkman will use sed to perform some text replacements. If you want to use a different
# command, you can set the following variable.
#
# SDKMAN_SED_COMMAND="sed"
#
