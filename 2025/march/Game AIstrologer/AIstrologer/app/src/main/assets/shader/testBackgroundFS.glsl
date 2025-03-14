#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform vec2 u_groupSize;
uniform vec2 u_direction;
uniform float u_blurAmount;

void main() {
    vec4 sum = vec4(0.0);
    vec2 tex_offset = (u_direction * u_blurAmount) / u_groupSize;

    float weights[11];
    weights[0] = 0.05; weights[1] = 0.06; weights[2] = 0.08; weights[3] = 0.11; weights[4] = 0.15;
    weights[5] = 0.20; weights[6] = 0.15; weights[7] = 0.11; weights[8] = 0.08; weights[9] = 0.06; weights[10] = 0.05;

    for (int i = -5; i <= 5; i++) {
        sum += texture2D(u_texture, v_texCoords + float(i) * tex_offset) * weights[i + 5];
    }

    gl_FragColor = sum * v_color;
}