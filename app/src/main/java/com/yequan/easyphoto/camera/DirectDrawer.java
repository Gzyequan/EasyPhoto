package com.yequan.easyphoto.camera;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES30;

import com.yequan.easyphoto.utils.ShaderUtils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static com.yequan.easyphoto.utils.FileUtils.loadFromAssets;


/**
 * Created by Administrator on 2017/6/5 0005.
 */

public class DirectDrawer {
    private FloatBuffer vertexBuffer, textureVerticesBuffer;
    private ShortBuffer drawOrderBuffer;
    private int mProgram;
    private int mTextureId;

    private float[] vertexArray = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, -1.0f,
            1.0f, 1.0f
    };

    private float[] textureArray = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f
    };

    private short[] drawOrderArray = {
            0, 1, 2,
            0, 2, 3
    };

    public DirectDrawer(Context context, int textureId) {
        this.mTextureId = textureId;

        vertexBuffer = ShaderUtils.createFloatBuffer(vertexArray);
        textureVerticesBuffer = ShaderUtils.createFloatBuffer(textureArray);
        drawOrderBuffer = ShaderUtils.createShortBuffer(drawOrderArray);

        String vertexShaderCode = loadFromAssets("vertexshader.glsl", context.getResources());
        String fragmentShaderCode = loadFromAssets("fragmentshader.glsl", context.getResources());

        int vertexShader = ShaderUtils.loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = ShaderUtils.loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = ShaderUtils.createProgram(vertexShader, fragmentShader);
    }

    public void drawSelf() {
        GLES30.glUseProgram(mProgram);

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureId);

        int positionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition");
        GLES30.glVertexAttribPointer(positionHandle, 2, GLES30.GL_FLOAT, false, 2 * 4, vertexBuffer);
        GLES30.glEnableVertexAttribArray(positionHandle);

        int textureHandle = GLES30.glGetAttribLocation(mProgram, "inputTextureCoordinate");
        GLES30.glVertexAttribPointer(textureHandle, 2, GLES30.GL_FLOAT, false, 2 * 4, textureVerticesBuffer);
        GLES30.glEnableVertexAttribArray(textureHandle);

        GLES30.glDrawElements(GLES30.GL_TRIANGLES, drawOrderArray.length, GLES30.GL_UNSIGNED_SHORT, drawOrderBuffer);

        GLES30.glDisableVertexAttribArray(positionHandle);
        GLES30.glDisableVertexAttribArray(textureHandle);
    }


}
