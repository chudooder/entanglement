#version 120

varying vec4 vertColor;
uniform sampler2D texture0;
uniform sampler2D texture1;

uniform float arg0;
uniform float arg1;
uniform float arg2;

void main() {
	vec3 norm = normalize(texture2D(texture1, gl_TexCoord[0].st).rgb * 2.0 - 1.0);
	vec3 lightvec = vec3(arg0, arg1, arg2);
	float dist = length(lightvec); 
 
	vec3 lightVector = normalize(lightvec); 
	float nxDir = -dot(norm, lightVector);
	
	vec4 finalColor = texture2D(texture0, gl_TexCoord[0].st);
	finalColor.r = finalColor.r + nxDir;
	finalColor.g = finalColor.g + nxDir;
	finalColor.b = finalColor.b + nxDir;
    gl_FragColor = finalColor;
}
