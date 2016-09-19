use <bitbeam-beam.scad>;

beam(4);
rotate([0, 0, 90])
	translate([0, -8, 0])
		beam(4);