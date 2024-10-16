package com.ff0044.engine.graph;

import org.lwjgl.opengl.GL30;
import com.ff0044.engine.Utils;
import org.tinylog.Logger;

import javax.management.RuntimeErrorException;
import java.util.*;

import static org.lwjgl.opengl.GL30.*;

public class ShaderProgram {
    private final int programId;

    public ShaderProgram(List<ShaderModuleData> shaderModuleDataList) {
        programId = glCreateProgram();
        if (programId==0) {
            Logger.error("Could not create shader", new RuntimeException());
            throw new RuntimeException();
        }

        List<Integer> shaderModules = new ArrayList<>();
        shaderModuleDataList.forEach(s -> shaderModules.add(createShader(Utils.readFile(s.shaderFile), s.shaderType)));
        link(shaderModules);
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void cleanup() {
        unbind();
        if (programId == 0) {
            glDeleteProgram(programId);
        }
    }

    protected int createShader(String shaderCode, int shaderType) {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            Logger.error("Error creating shader. Type: " + shaderType, new RuntimeException());
            throw new RuntimeException();
        }

        // Log the shader code for debugging
        Logger.debug("Compiling shader code:\n" + shaderCode);

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            Logger.error("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024), new RuntimeException());
            throw new RuntimeException();
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    public int getProgramId() {
        return programId;
    }

    private void link(List<Integer> shaderModules) {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            Logger.error("Error linking shader code: " + glGetProgramInfoLog(programId, 1024), new RuntimeException());
            throw new RuntimeException();
        }

        shaderModules.forEach(s->glDetachShader(programId, s));
        shaderModules.forEach(GL30::glDeleteShader);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void validate() {
        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            Logger.error("Error validating shader code: " + glGetProgramInfoLog(programId, 1024), new RuntimeException());
            throw new RuntimeException();
        }
    }

    public record ShaderModuleData(String shaderFile, int shaderType) {}
}
