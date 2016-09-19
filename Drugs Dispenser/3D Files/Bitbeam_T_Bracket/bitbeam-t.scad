use <bitbeam-beam.scad>;

beam(4);
rotate([0, 0, 90])
	translate([-(8 * 3), -8, 0])
		beam(7);