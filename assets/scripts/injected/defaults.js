
"use strict";

let scriptName = "packages.js"
let modName = "tantros"

let planetDebug = (val)=> {
    PlanetDialog.debugSelect = val;
    PlanetDialog.debugSectorAttackEdit = val;
    PlanetDialog.debugShowNumbers = val;
}

let that = ()=>{
    return Vars.world.tile(Vars.player.mouseX / Vars.tilesize, Vars.player.mouseY / Vars.tilesize);
}

let sorterMandelbrot = ()=>{
	var s=50, off=50, m=100,
		cl=[
			Items.lead,Items.titanium,Items.surgeAlloy,
			Items.coal,Items.plastanium,Items.silicon,
			Items.metaglass,Items.sand,Items.pyratite,
			Items.blastCompound,Items.sporePod,Items.copper,
			Items.phaseFabric,Items.scrap,Items.graphite,Items.beryllium
		];
	for(var i=0;i<s;i++){
		for(var j=0;j<s;j++){
			var cx=-2+(i/s)*2.5,cy=-1.25+(j/s)*2.5,x=0,y=0,it=0;
			while(x*x+y*y<=16&&it<m){
				var t=x*x-y*y+cx;y=2*x*y+cy;x=t;it++
			}
			var tl=Vars.world.tile(off+i,off+j);
			if(tl!=null){
				tl.setBlock(Blocks.sorter,Vars.player.team());
				if(tl.build){
					var itm=it<m?cl[Math.floor(it)%cl.length]:Items.coal;
					tl.build.configure(itm)
				}
			}
		}
	}
}
let sorterJulia = ()=>{let cx=-0.7,cy=0.27,s=80,m=Vars.content.items().size-1,p=Vars.player,sx=Math.floor(p.x/8),sy=Math.floor(p.y/8);for(let j=0;j<s;j++){for(let i=0;i<s;i++){let x=i/s*3-1.5,y=j/s*3-1.5,it=0;while(x*x+y*y<4&&it<m){let t=x*x-y*y+cx;y=2*x*y+cy;x=t;it++}let t=Vars.world.tile(sx+i,sy+j);t.setNet(Blocks.sorter,p.team(),0);let b=t.build;if(b)b.configure(Vars.content.items().get(it))}}}