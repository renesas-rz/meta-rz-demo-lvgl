# meta-rz-demo-lvgl  
yocto meta layer for lvgl demo  
  
Dependencies  
============  
  
The following meta packages are required:  
poky  
meta-renesas  
meta-openembedded  
**meta-rz-pmod0-ili9341** [**option**] add SPI-display plus touch device  
  
**Application tested on following boards:**  
  
   *    Renesas RZFive-EVK [rzfive_vlp_v3.0.2]  
   *    Renesas RZG2L-EVK [rzg_bsp_v3.0.1]  
   *    Renesas RZG2UL-EVK [rzg_bsp_v3.0.1]  
   *    Renesas RZG2VL-EVK [rzv_bsp_v3.0.2]  
  

Patches  
=======  
 
To contribute to this layer you should email patches to renesas-rz@renesas.com.  
Please send .patch files as email attachments, not embedded in the email body. 
  
Table of Contents  
=================  
  
    I.   Adding the meta-rz-demo-lvgl layer  
    II.  Usage of the lvgldemo  
    III. Misc  
  
I. Adding the meta-rz-demo-lvgl layer  
=====================================  
  
Please add the configuration lines at the end of the related  
configuration files    
  
**local.conf**  
  
*IMAGE_INSTALL_append = " lvgldemo "*  
  
**bblayers.conf**  
  
*BBLAYERS += " ${TOPDIR}/../meta-rz-demo-lvgl "*  
  
  
II. Usage  
=========  
  
**$** lvgldemo -h  
  
usage: lvgldemo  
>   --primary,     -p  run the demo on the primary display connected to LCDC (fbdev or weston)  
>   --secondary,   -s  run the demo on SPI connected frame buffer display  
>   --window,      -w  run the demo in a window instead of full screen (weston required)  
>   --music,       -m  run optional Music-demo  
>   --stress,      -t  run optional Stress-demo  
>   --benchmark,   -b  run optional Benchmark-demo  
>  
>   --help,        -h  print usage  
  
**Note:** 
> lvgl widget demo is the default except --music, --stress and --benchmark  
> options are used.  
> In case the program will be started without options it tries to use the  
> secondary display first, before switching to the primary one.  
> Supported color formats are RGB565 and ARGB8888 by auto detection.
  
  
III. Misc  
=========  
  

A secondary display is supported by the lvgldemo too.  
The following are the output device assignments:  
  
| EVK    | meta-rz-pmod0-ili9341| primary display| secondary display|
|--------|----------------------|----------------|------------------|
| RZG2L  | not used             | /dev/fb0       | N/A              |
| RZG2L  | used                 | /dev/fb1       | /dev/fb0         |
| RZV2L  | not used             | /dev/fb0       | N/A              |
| RZV2L  | used                 | /dev/fb1       | /dev/fb0         |
| RZG2UL | not used             | N/A            | N/A              |
| RZG2UL | used                 | N/A            | /dev/fb0         |
| RZFIVE | not used             | N/A            | N/A              |
| RZFIVE | used                 | N/A            | /dev/fb0         |
  
The lvgldemo supports the following devices for output:  
  
| EVK            | build  | /dev/fb0 | /dev/fb1 | Weston      | Weston window|
|----------------|--------|----------|----------|-------------|--------------|
| RZG2L, RZV2L   | minimal| YES      | YES      | N/A         | N/A          |
| RZG2L, RZV2L   | bsp    | YES      | YES      | N/A         | N/A          |
| RZG2L, RZV2L   | weston | YES      | YES      | YES(primary)| YES(primary) |
| RZG2L, RZV2L   | qt     | YES      | YES      | YES(primary)| YES(primary) |
| RZG2UL, RZVFIVE| minimal| YES      | N/A      | N/A         | N/A          |
| RZG2UL, RZVFIVE| bsp    | YES      | N/A      | N/A         | N/A          |
  
**Note:**  
>It is possible to run the demo on primary and secondary display independently  
>Touch support is possible for both devices.  

**Known issues:**  
>In Weston mode moving parts may show pixel errors.  
>This can not be fixed in this wrapper packet.  
