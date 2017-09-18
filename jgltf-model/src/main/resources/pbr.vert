attribute vec4 a_position;
attribute vec4 a_normal;
attribute vec4 a_tangent;

attribute vec2 a_baseColorTexCoord;
attribute vec2 a_metallicRoughnessTexCoord;
attribute vec2 a_normalTexCoord;
attribute vec2 a_occlusionTexCoord;
attribute vec2 a_emissiveTexCoord;

uniform mat4 u_modelViewMatrix;
uniform mat4 u_projectionMatrix;
uniform mat3 u_normalMatrix;

varying vec3 v_position;
varying vec3 v_normal;
varying vec4 v_tangent;

varying vec3 v_lightPosition;

varying vec2 v_baseColorTexCoord;
varying vec2 v_metallicRoughnessTexCoord;
varying vec2 v_normalTexCoord;
varying vec2 v_occlusionTexCoord;
varying vec2 v_emissiveTexCoord;

// TODO Only for basic tests
//attribute vec4 a_joint;
//attribute vec4 a_weight;
//uniform mat4 u_jointMat[16];


void main()
{
/*
    // TODO Only for basic tests
    mat4 skinMat = a_weight.x * u_jointMat[int(a_joint.x)];
    skinMat += a_weight.y * u_jointMat[int(a_joint.y)];
    skinMat += a_weight.z * u_jointMat[int(a_joint.z)];
    skinMat += a_weight.w * u_jointMat[int(a_joint.w)];
    vec4 pos = u_modelViewMatrix * skinMat * a_position;
*/
    vec4 pos = u_modelViewMatrix * a_position;
    v_position = vec3(pos.xyz) / pos.w;
    v_normal = u_normalMatrix * vec3(a_normal);
    v_tangent = vec4(u_normalMatrix * vec3(a_tangent), a_tangent.w);

    vec3 lightPosition = vec3(-800,500,-500);
    v_lightPosition = vec3(u_modelViewMatrix * vec4(lightPosition, 1.0));

    v_baseColorTexCoord = a_baseColorTexCoord;
    v_metallicRoughnessTexCoord = a_metallicRoughnessTexCoord;
    v_normalTexCoord = a_normalTexCoord;
    v_occlusionTexCoord = a_occlusionTexCoord;
    v_emissiveTexCoord = a_emissiveTexCoord;

    gl_Position = u_projectionMatrix * vec4(v_position, 1.0);
}


