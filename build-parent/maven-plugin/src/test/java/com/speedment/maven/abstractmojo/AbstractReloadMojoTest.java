/**
 *
 * Copyright (c) 2006-2017, Speedment, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.speedment.maven.abstractmojo;

import com.speedment.runtime.core.Speedment;
import com.speedment.tool.core.exception.SpeedmentToolException;
import com.speedment.tool.core.internal.util.ConfigFileHelper;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.nio.file.Paths;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigFileHelper.class})
public class AbstractReloadMojoTest {

    private AbstractReloadMojoTestImpl mojo;

    private String mockedConfigLocation = "testFile.txt";
    @Mock
    private Speedment mockedSpeedment;
    @Mock
    private ConfigFileHelper mockedConfigFileHelper;
    @Mock
    private MavenProject mockedMavenProjecct;

    @Before
    public void setup() {
        when(mockedMavenProjecct.getBasedir()).thenReturn(new File("baseDir"));

        mojo = new AbstractReloadMojoTestImpl() {
            @Override
            protected MavenProject project() {
                return mockedMavenProjecct;
            }

        };
    }

    @Test
    public void execute() throws Exception {
        // Given
        when(mockedSpeedment.getOrThrow(ConfigFileHelper.class)).thenReturn(mockedConfigFileHelper);
        mojo.setConfigFile(mockedConfigLocation);

        // When
        mojo.execute(mockedSpeedment);

        // Then
        verify(mockedConfigFileHelper).setCurrentlyOpenFile(Paths.get("baseDir", mockedConfigLocation).toFile());
        verify(mockedConfigFileHelper).loadFromDatabaseAndSaveToFile();
    }

    @Test(expected = MojoExecutionException.class)
    public void executeSetCurrentlyOpenFileSpeedmentToolException() throws Exception {
        // Given
        when(mockedSpeedment.getOrThrow(ConfigFileHelper.class)).thenReturn(mockedConfigFileHelper);
        mojo.setConfigFile(mockedConfigLocation);
        doThrow(new SpeedmentToolException("test exception")).when(mockedConfigFileHelper).setCurrentlyOpenFile(any(File.class));

        // When
        mojo.execute(mockedSpeedment);

        // Then
        verify(mockedConfigFileHelper).setCurrentlyOpenFile(Paths.get("baseDir", mockedConfigLocation).toFile());
        verify(mockedConfigFileHelper).loadFromDatabaseAndSaveToFile();
    }

    @Test(expected = MojoExecutionException.class)
    public void executeLoadFromDatabaseAndSaveToFileSpeedmentToolException() throws Exception {
        // Given
        when(mockedSpeedment.getOrThrow(ConfigFileHelper.class)).thenReturn(mockedConfigFileHelper);
        mojo.setConfigFile(mockedConfigLocation);
        doThrow(new SpeedmentToolException("test exception")).when(mockedConfigFileHelper).loadFromDatabaseAndSaveToFile();

        // When
        mojo.execute(mockedSpeedment);

        // Then
        verify(mockedConfigFileHelper).setCurrentlyOpenFile(Paths.get("baseDir", mockedConfigLocation).toFile());
        verify(mockedConfigFileHelper).loadFromDatabaseAndSaveToFile();
    }

}
