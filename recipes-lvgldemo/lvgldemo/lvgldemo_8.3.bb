SUMMARY = "LVGL for frame buffer device demo application ( executable mapped to: lvgl_demo )"
HOMEPAGE = "https://blog.lvgl.io/2018-01-03/linux_fb"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=802d3d83ae80ef5f343050bf96cce3a4 \
                    file://lv_drivers/LICENSE;md5=d6fc0df890c5270ef045981b516bb8f2 \
                    file://lvgl/LICENCE.txt;md5=bf1198c89ae87f043108cea62460b03a"

SRC_URI[sha256sum] = "52d1b69ea1d99d6bc80ca0980394be44314b1a757893f0af75c5afaccac9d307"

SRC_URI = "\
       gitsm://github.com/lvgl/lv_port_linux_frame_buffer.git;branch=master;protocol=https;name=maindemo \
       file://0001-adapt-for-external-libraries-and-Yocto-env.patch \
       file://0002-add-bit-color-control-and-increase-memory-footprint.patch \
       file://0003-change-entries-for-automatic-device-detection.patch \
       file://0004-make-device-recognition-select-able-and-set-input-de.patch \
       file://0005-add-Music-Stress-and-Benchmark-demo.patch \
       file://0006-adapt-to-changed-interface.patch \
       file://0010-fbdev-make-device-selectable-and-disable-blank-check.patch \
       file://0011-evdev-add-auto-device-Add-absolute-recognition-for.patch \
       file://0012-libinput-allow-unhandled-devices.patch \
"

SRCREV_maindemo = "adf2c4490e17a1b9ec1902cc412a24b3b8235c8e" 
#PV = "1.0+git${SRCPV}"

DEPENDS  = "libinput udev "
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'libxkbcommon ', '', d)}"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland-native ', '', d)}"

S = "${WORKDIR}/git"

LDFLAGS_prepend = "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', '-linput -lwayland-client -lxkbcommon -lwayland-cursor ', '-linput ', d)}"

do_compile() {
    oe_runmake clean 
    oe_runmake UserDefined="-D LV_COLOR_DEPTH_BY_MAKE=32 -D USE_LIBINPUT=1 -D USE_EVDEV=0 -D USE_WAYLAND=0 -D LV_DEMO_MUSIC_LARGE=1" BIN=lvgldemo_32FP all 
    oe_runmake clean BIN=lvgldemo_not_exists
    oe_runmake UserDefined="-D LV_COLOR_DEPTH_BY_MAKE=16 -D USE_LIBINPUT=1 -D USE_EVDEV=0 -D USE_WAYLAND=0 -D LV_DEMO_MUSIC_LARGE=1" BIN=lvgldemo_16FP all 
    oe_runmake clean BIN=lvgldemo_not_exists
    oe_runmake UserDefined="-D LV_COLOR_DEPTH_BY_MAKE=32 -D USE_LIBINPUT=0 -D USE_EVDEV=1 -D USE_WAYLAND=0 -D LV_DEMO_MUSIC_LARGE=0" BIN=lvgldemo_32FS all 
    oe_runmake clean BIN=lvgldemo_not_exists
    oe_runmake UserDefined="-D LV_COLOR_DEPTH_BY_MAKE=16 -D USE_LIBINPUT=0 -D USE_EVDEV=1 -D USE_WAYLAND=0 -D LV_DEMO_MUSIC_LARGE=0" BIN=lvgldemo_16FS all 
    if [ "${@bb.utils.filter('DISTRO_FEATURES', 'wayland', d)}" ]; then
      oe_runmake clean BIN=lvgldemo_not_exists
      oe_runmake UserDefined="-D LV_COLOR_DEPTH_BY_MAKE=32 -D USE_LIBINPUT=0 -D USE_EVDEV=0 -D USE_FBDEV=0 -D USE_WAYLAND=1 -D LV_WAYLAND_WL_SHELL=1 -D LV_WAYLAND_XDG_SHELL=0 -D LV_WAYLAND_TIMER_HANDLER=1 -D LV_DEMO_MUSIC_LARGE=1" BIN=lvgldemo_32W all 
      oe_runmake clean BIN=lvgldemo_not_exists
      oe_runmake UserDefined="-D LV_COLOR_DEPTH_BY_MAKE=16 -D USE_LIBINPUT=0 -D USE_EVDEV=0 -D USE_FBDEV=0 -D USE_WAYLAND=1 -D LV_WAYLAND_WL_SHELL=1 -D LV_WAYLAND_XDG_SHELL=0 -D LV_WAYLAND_TIMER_HANDLER=1 -D LV_DEMO_MUSIC_LARGE=1" BIN=lvgldemo_16W all 
    fi
}

do_install() {
    install -d ${D}/usr/bin
    install -m 0755 ${S}/lvgldemo_32* ${D}/usr/bin/
    install -m 0755 ${S}/lvgldemo_16* ${D}/usr/bin/
    ln -sf /usr/bin/lvgldemo_16FS \
	       ${D}/usr/bin/lvgldemo 
}

# Can't be built with ccache
CCACHE_DISABLE = "1"
