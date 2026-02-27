
varying vec2 v_texCoords;
uniform sampler2D u_texture0;
uniform sampler2D u_texture1;

void main(){
    vec2 uv = v_texCoords;
    vec4 color = texture2D(u_texture0, uv);
    vec4 mask = texture2D(u_texture1, uv);

    if(color.a > 0.0 && mask.a < 1.0){
        color.a = mask.a;
    }

    gl_FragColor = color;
}