/*
 * Copyright (c) 2008 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN
 * MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR
 * ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 */

package demos.es1.cubefbo;

import javax.media.opengl.*;
import java.nio.*;

class FBCubes implements GLEventListener {
    private static final int FBO_SIZE = 128;

    public FBCubes () {
        cubeInner = new CubeObject(false);
        cubeMiddle = new CubeObject(true);
        cubeOuter = new CubeObject(true);

        //        cubeOuter = new CubeObject(false);

        fbo1 = new FBObject(FBO_SIZE, FBO_SIZE);
        fbo2 = new FBObject(FBO_SIZE, FBO_SIZE);
    }

    public void init(GLAutoDrawable drawable) {
        GL2ES1 gl = drawable.getGL().getGL2ES1();
        fbo1.init(gl);
        fbo2.init(gl);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2ES1 gl = drawable.getGL().getGL2ES1();
        cubeOuter.reshape(gl, x, y, width, height);
    }

    float xRot=0f;
    float yRot=0f;

    public void rotate(float xRot, float yRot) {
        this.xRot = xRot;
        this.yRot = yRot;
    }

    public void display(GLAutoDrawable drawable) {
        GL2ES1 gl = drawable.getGL().getGL2ES1();

        fbo1.bind(gl);
        cubeInner.reshape(gl, 0, 0, FBO_SIZE, FBO_SIZE);
        cubeInner.display(gl, xRot, yRot);
        fbo1.unbind(gl);

        FBObject tex = fbo1;
        FBObject rend = fbo2;

        int MAX_ITER = 1;

        for (int i = 0; i < MAX_ITER; i++) {
            rend.bind(gl);
            gl.glEnable (gl.GL_TEXTURE_2D);
            gl.glBindTexture(gl.GL_TEXTURE_2D, tex.getTextureName()); // to use it ..
            cubeMiddle.reshape(gl, 0, 0, FBO_SIZE, FBO_SIZE);
            cubeMiddle.display(gl, xRot, yRot);
            gl.glBindTexture(gl.GL_TEXTURE_2D, 0);
            gl.glDisable (gl.GL_TEXTURE_2D);
            rend.unbind(gl);
            FBObject tmp = tex;
            tex = rend;
            rend = tmp;
        }

        //        System.out.println("display .. p6");
        gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(0, 0, 0, 1);

        gl.glEnable (gl.GL_TEXTURE_2D);
        gl.glBindTexture(gl.GL_TEXTURE_2D, tex.getTextureName()); // to use it ..
        cubeOuter.display(gl, xRot, yRot);
        //        System.out.println("display .. p7");
        gl.glBindTexture(gl.GL_TEXTURE_2D, 0);
        gl.glDisable (gl.GL_TEXTURE_2D);
    }

    public void displayChanged(javax.media.opengl.GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
    
    float time = 0.0f;
    CubeObject cubeInner;
    CubeObject cubeMiddle;
    CubeObject cubeOuter;
    FBObject   fbo1;
    FBObject   fbo2;
}

