#ifdef GL_ES
    precision highp float;
#endif

attribute vec3 a_position;
attribute vec2 a_texcoord0;

uniform mat4 u_modelViewMatrix;
uniform mat4 u_projectionMatrix;

varying vec3 v_position;
varying vec2 v_texcoord0;

void main(void) 
{
    vec4 pos = u_modelViewMatrix * vec4(a_position,1);
    v_texcoord0 = a_texcoord0;
    v_position = pos.xyz;
    gl_Position = u_projectionMatrix * pos;
}
