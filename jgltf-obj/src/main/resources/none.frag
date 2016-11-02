#ifdef GL_ES
    precision highp float;
#endif

varying vec3 v_position;

uniform vec4 u_ambient;
uniform vec4 u_diffuse;
uniform vec4 u_specular;
uniform float u_shininess;

void main(void) 
{
    vec4 color = vec4(0, 0, 0, 0);
    vec4 ambient = u_ambient;
    vec4 diffuse = u_diffuse;
    vec4 specular = u_specular;
    color.xyz += ambient.xyz;
    color.xyz += diffuse.xyz;
    color.xyz += specular.xyz;
    color = vec4(color.rgb * diffuse.a, diffuse.a);
    gl_FragColor = color;
}
