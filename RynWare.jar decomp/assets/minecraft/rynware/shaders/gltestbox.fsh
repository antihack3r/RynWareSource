
#ifdef GL_ES
precision mediump float;
#endif

// glslsandbox uniforms
uniform float time;
uniform vec2 resolution;
uniform vec2 mouse;

// shadertoy emulation
#define IMouse mouse;
#define iTime time
#define iResolution vec3(resolution,1.)

const vec2 vp = vec2(326.0, 200.0);

const float PI = 3.14159265;

void mainImage( out vec4 fragColor, in vec2 fragCoord ) {
	// normalized pixel coordinates
	vec2 p = 3.0*fragCoord/iResolution.xy;

	// pattern
	float f = sin(p.x + sin(2.0*p.y + iTime * 0.2)) +
	sin(length(p)+iTime) +
	0.5*sin(p.x*2.5+iTime);

	// color
	vec3 col = 0.9 + 0.3*cos(f+vec3(0,3.1,4.2));

	// putput to screen
	fragColor = vec4(col,1.0);
}

void main(void){
    mainImage(gl_FragColor, gl_FragCoord.xy);
}