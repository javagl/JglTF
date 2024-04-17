#ifdef GL_ES
    precision highp float;
#endif

varying vec3 v_position;
varying vec2 v_texcoord0;
varying vec3 v_normal;

uniform vec4 u_ambient;
uniform vec4 u_specular;
uniform float u_shininess;

uniform sampler2D u_diffuse;

varying vec3 v_light0Direction;

void main(void) 
{
    vec3 normal = normalize(v_normal);
    vec4 color = vec4(0, 0, 0, 0);
    vec3 diffuseLight = vec3(0, 0, 0);
    vec3 lightColor = vec3(1,1,1);
    vec4 ambient = u_ambient;
    vec4 diffuse = texture2D(u_diffuse, v_texcoord0);
    vec4 specular = u_specular;

    vec3 specularLight = vec3(0, 0, 0);
    {
        float specularIntensity = 0;
        float attenuation = 1.0;
        vec3 l = normalize(v_light0Direction);
        vec3 viewDir = -normalize(v_position);
        vec3 h = normalize(l+viewDir);
        specularIntensity = max(0, pow(max(dot(normal,h), 0) , u_shininess)) * attenuation;
        specularLight += lightColor * specularIntensity;
        diffuseLight += lightColor * max(dot(normal,l), 0) * attenuation;
    }
    specular.xyz *= specularLight;
    diffuse.xyz *= diffuseLight;
    color.xyz += ambient.xyz;
    color.xyz += diffuse.xyz;
    color.xyz += specular.xyz;
    color = vec4(color.rgb * diffuse.a, diffuse.a);
    gl_FragColor = color;
}
