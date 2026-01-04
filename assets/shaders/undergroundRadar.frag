#define HIGHP

#define ALPHA 0.18
#define step 2.0

uniform sampler2D u_texture;

varying vec2 v_texCoords;

void main(){
    vec2 T = v_texCoords.xy;

    vec4 color = texture2D(u_texture, T);
    if(color.r > 0){
        color.r = 1;
    }
    if(color.g > 0){
        color.g = 1;
    }
    if(color.b > 0){
        color.b = 1;
    }

    gl_FragColor = color;
}
