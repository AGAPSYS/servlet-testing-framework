/*
 * Copyright 2017 Agapsys Tecnologia Ltda-ME.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.agapsys.jee;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractFileServlet extends HttpServlet {

    // <editor-fold desc="STATIC SCOPE" defaultstate="collapsed">
    public static final String DEFAULT_MIME = "application/octet-stream";
    public static final int DEFAULT_BUFFER_SIZE = 4096;

    private static final Map<String, String> MIME_MAP = new LinkedHashMap<>();

    static {
        MIME_MAP.put("123", "application/vnd.lotus-1-2-3");
        MIME_MAP.put("3dml", "text/vnd.in3d.3dml");
        MIME_MAP.put("3ds", "image/x-3ds");
        MIME_MAP.put("3g2", "video/3gpp2");
        MIME_MAP.put("3gp", "video/3gpp");
        MIME_MAP.put("7z", "application/x-7z-compressed");
        MIME_MAP.put("aab", "application/x-authorware-bin");
        MIME_MAP.put("aac", "audio/x-aac");
        MIME_MAP.put("aam", "application/x-authorware-map");
        MIME_MAP.put("aas", "application/x-authorware-seg");
        MIME_MAP.put("abs", "audio/x-mpeg");
        MIME_MAP.put("abw", "application/x-abiword");
        MIME_MAP.put("ac", "application/pkix-attr-cert");
        MIME_MAP.put("acc", "application/vnd.americandynamics.acc");
        MIME_MAP.put("ace", "application/x-ace-compressed");
        MIME_MAP.put("acu", "application/vnd.acucobol");
        MIME_MAP.put("acutc", "application/vnd.acucorp");
        MIME_MAP.put("adp", "audio/adpcm");
        MIME_MAP.put("aep", "application/vnd.audiograph");
        MIME_MAP.put("afm", "application/x-font-type1");
        MIME_MAP.put("afp", "application/vnd.ibm.modcap");
        MIME_MAP.put("ahead", "application/vnd.ahead.space");
        MIME_MAP.put("ai", "application/postscript");
        MIME_MAP.put("aif", "audio/x-aiff");
        MIME_MAP.put("aifc", "audio/x-aiff");
        MIME_MAP.put("aiff", "audio/x-aiff");
        MIME_MAP.put("aim", "application/x-aim");
        MIME_MAP.put("air", "application/vnd.adobe.air-application-installer-package+zip");
        MIME_MAP.put("ait", "application/vnd.dvb.ait");
        MIME_MAP.put("ami", "application/vnd.amiga.ami");
        MIME_MAP.put("anx", "application/annodex");
        MIME_MAP.put("apk", "application/vnd.android.package-archive");
        MIME_MAP.put("appcache", "text/cache-manifest");
        MIME_MAP.put("application", "application/x-ms-application");
        MIME_MAP.put("apr", "application/vnd.lotus-approach");
        MIME_MAP.put("arc", "application/x-freearc");
        MIME_MAP.put("art", "image/x-jg");
        MIME_MAP.put("asc", "application/pgp-signature");
        MIME_MAP.put("asf", "video/x-ms-asf");
        MIME_MAP.put("asm", "text/x-asm");
        MIME_MAP.put("aso", "application/vnd.accpac.simply.aso");
        MIME_MAP.put("asx", "video/x-ms-asf");
        MIME_MAP.put("atc", "application/vnd.acucorp");
        MIME_MAP.put("atom", "application/atom+xml");
        MIME_MAP.put("atomcat", "application/atomcat+xml");
        MIME_MAP.put("atomsvc", "application/atomsvc+xml");
        MIME_MAP.put("atx", "application/vnd.antix.game-component");
        MIME_MAP.put("au", "audio/basic");
        MIME_MAP.put("avi", "video/x-msvideo");
        MIME_MAP.put("avx", "video/x-rad-screenplay");
        MIME_MAP.put("aw", "application/applixware");
        MIME_MAP.put("axa", "audio/annodex");
        MIME_MAP.put("axv", "video/annodex");
        MIME_MAP.put("azf", "application/vnd.airzip.filesecure.azf");
        MIME_MAP.put("azs", "application/vnd.airzip.filesecure.azs");
        MIME_MAP.put("azw", "application/vnd.amazon.ebook");
        MIME_MAP.put("bat", "application/x-msdownload");
        MIME_MAP.put("bcpio", "application/x-bcpio");
        MIME_MAP.put("bdf", "application/x-font-bdf");
        MIME_MAP.put("bdm", "application/vnd.syncml.dm+wbxml");
        MIME_MAP.put("bed", "application/vnd.realvnc.bed");
        MIME_MAP.put("bh2", "application/vnd.fujitsu.oasysprs");
        MIME_MAP.put("bin", "application/octet-stream");
        MIME_MAP.put("blb", "application/x-blorb");
        MIME_MAP.put("blorb", "application/x-blorb");
        MIME_MAP.put("bmi", "application/vnd.bmi");
        MIME_MAP.put("bmp", "image/bmp");
        MIME_MAP.put("body", "text/html");
        MIME_MAP.put("book", "application/vnd.framemaker");
        MIME_MAP.put("box", "application/vnd.previewsystems.box");
        MIME_MAP.put("boz", "application/x-bzip2");
        MIME_MAP.put("bpk", "application/octet-stream");
        MIME_MAP.put("btif", "image/prs.btif");
        MIME_MAP.put("bz", "application/x-bzip");
        MIME_MAP.put("bz2", "application/x-bzip2");
        MIME_MAP.put("c", "text/x-c");
        MIME_MAP.put("c11amc", "application/vnd.cluetrust.cartomobile-config");
        MIME_MAP.put("c11amz", "application/vnd.cluetrust.cartomobile-config-pkg");
        MIME_MAP.put("c4d", "application/vnd.clonk.c4group");
        MIME_MAP.put("c4f", "application/vnd.clonk.c4group");
        MIME_MAP.put("c4g", "application/vnd.clonk.c4group");
        MIME_MAP.put("c4p", "application/vnd.clonk.c4group");
        MIME_MAP.put("c4u", "application/vnd.clonk.c4group");
        MIME_MAP.put("cab", "application/vnd.ms-cab-compressed");
        MIME_MAP.put("caf", "audio/x-caf");
        MIME_MAP.put("cap", "application/vnd.tcpdump.pcap");
        MIME_MAP.put("car", "application/vnd.curl.car");
        MIME_MAP.put("cat", "application/vnd.ms-pki.seccat");
        MIME_MAP.put("cb7", "application/x-cbr");
        MIME_MAP.put("cba", "application/x-cbr");
        MIME_MAP.put("cbr", "application/x-cbr");
        MIME_MAP.put("cbt", "application/x-cbr");
        MIME_MAP.put("cbz", "application/x-cbr");
        MIME_MAP.put("cc", "text/x-c");
        MIME_MAP.put("cct", "application/x-director");
        MIME_MAP.put("ccxml", "application/ccxml+xml");
        MIME_MAP.put("cdbcmsg", "application/vnd.contact.cmsg");
        MIME_MAP.put("cdf", "application/x-cdf");
        MIME_MAP.put("cdkey", "application/vnd.mediastation.cdkey");
        MIME_MAP.put("cdmia", "application/cdmi-capability");
        MIME_MAP.put("cdmic", "application/cdmi-container");
        MIME_MAP.put("cdmid", "application/cdmi-domain");
        MIME_MAP.put("cdmio", "application/cdmi-object");
        MIME_MAP.put("cdmiq", "application/cdmi-queue");
        MIME_MAP.put("cdx", "chemical/x-cdx");
        MIME_MAP.put("cdxml", "application/vnd.chemdraw+xml");
        MIME_MAP.put("cdy", "application/vnd.cinderella");
        MIME_MAP.put("cer", "application/pkix-cert");
        MIME_MAP.put("cfs", "application/x-cfs-compressed");
        MIME_MAP.put("cgm", "image/cgm");
        MIME_MAP.put("chat", "application/x-chat");
        MIME_MAP.put("chm", "application/vnd.ms-htmlhelp");
        MIME_MAP.put("chrt", "application/vnd.kde.kchart");
        MIME_MAP.put("cif", "chemical/x-cif");
        MIME_MAP.put("cii", "application/vnd.anser-web-certificate-issue-initiation");
        MIME_MAP.put("cil", "application/vnd.ms-artgalry");
        MIME_MAP.put("cla", "application/vnd.claymore");
        MIME_MAP.put("class", "application/java");
        MIME_MAP.put("clkk", "application/vnd.crick.clicker.keyboard");
        MIME_MAP.put("clkp", "application/vnd.crick.clicker.palette");
        MIME_MAP.put("clkt", "application/vnd.crick.clicker.template");
        MIME_MAP.put("clkw", "application/vnd.crick.clicker.wordbank");
        MIME_MAP.put("clkx", "application/vnd.crick.clicker");
        MIME_MAP.put("clp", "application/x-msclip");
        MIME_MAP.put("cmc", "application/vnd.cosmocaller");
        MIME_MAP.put("cmdf", "chemical/x-cmdf");
        MIME_MAP.put("cml", "chemical/x-cml");
        MIME_MAP.put("cmp", "application/vnd.yellowriver-custom-menu");
        MIME_MAP.put("cmx", "image/x-cmx");
        MIME_MAP.put("cod", "application/vnd.rim.cod");
        MIME_MAP.put("com", "application/x-msdownload");
        MIME_MAP.put("conf", "text/plain");
        MIME_MAP.put("cpio", "application/x-cpio");
        MIME_MAP.put("cpp", "text/x-c");
        MIME_MAP.put("cpt", "application/mac-compactpro");
        MIME_MAP.put("crd", "application/x-mscardfile");
        MIME_MAP.put("crl", "application/pkix-crl");
        MIME_MAP.put("crt", "application/x-x509-ca-cert");
        MIME_MAP.put("cryptonote", "application/vnd.rig.cryptonote");
        MIME_MAP.put("csh", "application/x-csh");
        MIME_MAP.put("csml", "chemical/x-csml");
        MIME_MAP.put("csp", "application/vnd.commonspace");
        MIME_MAP.put("css", "text/css");
        MIME_MAP.put("cst", "application/x-director");
        MIME_MAP.put("csv", "text/csv");
        MIME_MAP.put("cu", "application/cu-seeme");
        MIME_MAP.put("curl", "text/vnd.curl");
        MIME_MAP.put("cww", "application/prs.cww");
        MIME_MAP.put("cxt", "application/x-director");
        MIME_MAP.put("cxx", "text/x-c");
        MIME_MAP.put("dae", "model/vnd.collada+xml");
        MIME_MAP.put("daf", "application/vnd.mobius.daf");
        MIME_MAP.put("dart", "application/vnd.dart");
        MIME_MAP.put("dataless", "application/vnd.fdsn.seed");
        MIME_MAP.put("davmount", "application/davmount+xml");
        MIME_MAP.put("dbk", "application/docbook+xml");
        MIME_MAP.put("dcr", "application/x-director");
        MIME_MAP.put("dcurl", "text/vnd.curl.dcurl");
        MIME_MAP.put("dd2", "application/vnd.oma.dd2+xml");
        MIME_MAP.put("ddd", "application/vnd.fujixerox.ddd");
        MIME_MAP.put("deb", "application/x-debian-package");
        MIME_MAP.put("def", "text/plain");
        MIME_MAP.put("deploy", "application/octet-stream");
        MIME_MAP.put("der", "application/x-x509-ca-cert");
        MIME_MAP.put("dfac", "application/vnd.dreamfactory");
        MIME_MAP.put("dgc", "application/x-dgc-compressed");
        MIME_MAP.put("dib", "image/bmp");
        MIME_MAP.put("dic", "text/x-c");
        MIME_MAP.put("dir", "application/x-director");
        MIME_MAP.put("dis", "application/vnd.mobius.dis");
        MIME_MAP.put("dist", "application/octet-stream");
        MIME_MAP.put("distz", "application/octet-stream");
        MIME_MAP.put("djv", "image/vnd.djvu");
        MIME_MAP.put("djvu", "image/vnd.djvu");
        MIME_MAP.put("dll", "application/x-msdownload");
        MIME_MAP.put("dmg", "application/x-apple-diskimage");
        MIME_MAP.put("dmp", "application/vnd.tcpdump.pcap");
        MIME_MAP.put("dms", "application/octet-stream");
        MIME_MAP.put("dna", "application/vnd.dna");
        MIME_MAP.put("doc", "application/msword");
        MIME_MAP.put("docm", "application/vnd.ms-word.document.macroenabled.12");
        MIME_MAP.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        MIME_MAP.put("dot", "application/msword");
        MIME_MAP.put("dotm", "application/vnd.ms-word.template.macroenabled.12");
        MIME_MAP.put("dotx", "application/vnd.openxmlformats-officedocument.wordprocessingml.template");
        MIME_MAP.put("dp", "application/vnd.osgi.dp");
        MIME_MAP.put("dpg", "application/vnd.dpgraph");
        MIME_MAP.put("dra", "audio/vnd.dra");
        MIME_MAP.put("dsc", "text/prs.lines.tag");
        MIME_MAP.put("dssc", "application/dssc+der");
        MIME_MAP.put("dtb", "application/x-dtbook+xml");
        MIME_MAP.put("dtd", "application/xml-dtd");
        MIME_MAP.put("dts", "audio/vnd.dts");
        MIME_MAP.put("dtshd", "audio/vnd.dts.hd");
        MIME_MAP.put("dump", "application/octet-stream");
        MIME_MAP.put("dv", "video/x-dv");
        MIME_MAP.put("dvb", "video/vnd.dvb.file");
        MIME_MAP.put("dvi", "application/x-dvi");
        MIME_MAP.put("dwf", "model/vnd.dwf");
        MIME_MAP.put("dwg", "image/vnd.dwg");
        MIME_MAP.put("dxf", "image/vnd.dxf");
        MIME_MAP.put("dxp", "application/vnd.spotfire.dxp");
        MIME_MAP.put("dxr", "application/x-director");
        MIME_MAP.put("ecelp4800", "audio/vnd.nuera.ecelp4800");
        MIME_MAP.put("ecelp7470", "audio/vnd.nuera.ecelp7470");
        MIME_MAP.put("ecelp9600", "audio/vnd.nuera.ecelp9600");
        MIME_MAP.put("ecma", "application/ecmascript");
        MIME_MAP.put("edm", "application/vnd.novadigm.edm");
        MIME_MAP.put("edx", "application/vnd.novadigm.edx");
        MIME_MAP.put("efif", "application/vnd.picsel");
        MIME_MAP.put("ei6", "application/vnd.pg.osasli");
        MIME_MAP.put("elc", "application/octet-stream");
        MIME_MAP.put("emf", "application/x-msmetafile");
        MIME_MAP.put("eml", "message/rfc822");
        MIME_MAP.put("emma", "application/emma+xml");
        MIME_MAP.put("emz", "application/x-msmetafile");
        MIME_MAP.put("eol", "audio/vnd.digital-winds");
        MIME_MAP.put("eot", "application/vnd.ms-fontobject");
        MIME_MAP.put("eps", "application/postscript");
        MIME_MAP.put("epub", "application/epub+zip");
        MIME_MAP.put("es3", "application/vnd.eszigno3+xml");
        MIME_MAP.put("esa", "application/vnd.osgi.subsystem");
        MIME_MAP.put("esf", "application/vnd.epson.esf");
        MIME_MAP.put("et3", "application/vnd.eszigno3+xml");
        MIME_MAP.put("etx", "text/x-setext");
        MIME_MAP.put("eva", "application/x-eva");
        MIME_MAP.put("evy", "application/x-envoy");
        MIME_MAP.put("exe", "application/octet-stream");
        MIME_MAP.put("exi", "application/exi");
        MIME_MAP.put("ext", "application/vnd.novadigm.ext");
        MIME_MAP.put("ez", "application/andrew-inset");
        MIME_MAP.put("ez2", "application/vnd.ezpix-album");
        MIME_MAP.put("ez3", "application/vnd.ezpix-package");
        MIME_MAP.put("f", "text/x-fortran");
        MIME_MAP.put("f4v", "video/x-f4v");
        MIME_MAP.put("f77", "text/x-fortran");
        MIME_MAP.put("f90", "text/x-fortran");
        MIME_MAP.put("fbs", "image/vnd.fastbidsheet");
        MIME_MAP.put("fcdt", "application/vnd.adobe.formscentral.fcdt");
        MIME_MAP.put("fcs", "application/vnd.isac.fcs");
        MIME_MAP.put("fdf", "application/vnd.fdf");
        MIME_MAP.put("fe_launch", "application/vnd.denovo.fcselayout-link");
        MIME_MAP.put("fg5", "application/vnd.fujitsu.oasysgp");
        MIME_MAP.put("fgd", "application/x-director");
        MIME_MAP.put("fh", "image/x-freehand");
        MIME_MAP.put("fh4", "image/x-freehand");
        MIME_MAP.put("fh5", "image/x-freehand");
        MIME_MAP.put("fh7", "image/x-freehand");
        MIME_MAP.put("fhc", "image/x-freehand");
        MIME_MAP.put("fig", "application/x-xfig");
        MIME_MAP.put("flac", "audio/flac");
        MIME_MAP.put("fli", "video/x-fli");
        MIME_MAP.put("flo", "application/vnd.micrografx.flo");
        MIME_MAP.put("flv", "video/x-flv");
        MIME_MAP.put("flw", "application/vnd.kde.kivio");
        MIME_MAP.put("flx", "text/vnd.fmi.flexstor");
        MIME_MAP.put("fly", "text/vnd.fly");
        MIME_MAP.put("fm", "application/vnd.framemaker");
        MIME_MAP.put("fnc", "application/vnd.frogans.fnc");
        MIME_MAP.put("for", "text/x-fortran");
        MIME_MAP.put("fpx", "image/vnd.fpx");
        MIME_MAP.put("frame", "application/vnd.framemaker");
        MIME_MAP.put("fsc", "application/vnd.fsc.weblaunch");
        MIME_MAP.put("fst", "image/vnd.fst");
        MIME_MAP.put("ftc", "application/vnd.fluxtime.clip");
        MIME_MAP.put("fti", "application/vnd.anser-web-funds-transfer-initiation");
        MIME_MAP.put("fvt", "video/vnd.fvt");
        MIME_MAP.put("fxp", "application/vnd.adobe.fxp");
        MIME_MAP.put("fxpl", "application/vnd.adobe.fxp");
        MIME_MAP.put("fzs", "application/vnd.fuzzysheet");
        MIME_MAP.put("g2w", "application/vnd.geoplan");
        MIME_MAP.put("g3", "image/g3fax");
        MIME_MAP.put("g3w", "application/vnd.geospace");
        MIME_MAP.put("gac", "application/vnd.groove-account");
        MIME_MAP.put("gam", "application/x-tads");
        MIME_MAP.put("gbr", "application/rpki-ghostbusters");
        MIME_MAP.put("gca", "application/x-gca-compressed");
        MIME_MAP.put("gdl", "model/vnd.gdl");
        MIME_MAP.put("geo", "application/vnd.dynageo");
        MIME_MAP.put("gex", "application/vnd.geometry-explorer");
        MIME_MAP.put("ggb", "application/vnd.geogebra.file");
        MIME_MAP.put("ggt", "application/vnd.geogebra.tool");
        MIME_MAP.put("ghf", "application/vnd.groove-help");
        MIME_MAP.put("gif", "image/gif");
        MIME_MAP.put("gim", "application/vnd.groove-identity-message");
        MIME_MAP.put("gml", "application/gml+xml");
        MIME_MAP.put("gmx", "application/vnd.gmx");
        MIME_MAP.put("gnumeric", "application/x-gnumeric");
        MIME_MAP.put("gph", "application/vnd.flographit");
        MIME_MAP.put("gpx", "application/gpx+xml");
        MIME_MAP.put("gqf", "application/vnd.grafeq");
        MIME_MAP.put("gqs", "application/vnd.grafeq");
        MIME_MAP.put("gram", "application/srgs");
        MIME_MAP.put("gramps", "application/x-gramps-xml");
        MIME_MAP.put("gre", "application/vnd.geometry-explorer");
        MIME_MAP.put("grv", "application/vnd.groove-injector");
        MIME_MAP.put("grxml", "application/srgs+xml");
        MIME_MAP.put("gsf", "application/x-font-ghostscript");
        MIME_MAP.put("gtar", "application/x-gtar");
        MIME_MAP.put("gtm", "application/vnd.groove-tool-message");
        MIME_MAP.put("gtw", "model/vnd.gtw");
        MIME_MAP.put("gv", "text/vnd.graphviz");
        MIME_MAP.put("gxf", "application/gxf");
        MIME_MAP.put("gxt", "application/vnd.geonext");
        MIME_MAP.put("gz", "application/x-gzip");
        MIME_MAP.put("h", "text/x-c");
        MIME_MAP.put("h261", "video/h261");
        MIME_MAP.put("h263", "video/h263");
        MIME_MAP.put("h264", "video/h264");
        MIME_MAP.put("hal", "application/vnd.hal+xml");
        MIME_MAP.put("hbci", "application/vnd.hbci");
        MIME_MAP.put("hdf", "application/x-hdf");
        MIME_MAP.put("hh", "text/x-c");
        MIME_MAP.put("hlp", "application/winhlp");
        MIME_MAP.put("hpgl", "application/vnd.hp-hpgl");
        MIME_MAP.put("hpid", "application/vnd.hp-hpid");
        MIME_MAP.put("hps", "application/vnd.hp-hps");
        MIME_MAP.put("hqx", "application/mac-binhex40");
        MIME_MAP.put("htc", "text/x-component");
        MIME_MAP.put("htke", "application/vnd.kenameaapp");
        MIME_MAP.put("htm", "text/html");
        MIME_MAP.put("html", "text/html");
        MIME_MAP.put("hvd", "application/vnd.yamaha.hv-dic");
        MIME_MAP.put("hvp", "application/vnd.yamaha.hv-voice");
        MIME_MAP.put("hvs", "application/vnd.yamaha.hv-script");
        MIME_MAP.put("i2g", "application/vnd.intergeo");
        MIME_MAP.put("icc", "application/vnd.iccprofile");
        MIME_MAP.put("ice", "x-conference/x-cooltalk");
        MIME_MAP.put("icm", "application/vnd.iccprofile");
        MIME_MAP.put("ico", "image/x-icon");
        MIME_MAP.put("ics", "text/calendar");
        MIME_MAP.put("ief", "image/ief");
        MIME_MAP.put("ifb", "text/calendar");
        MIME_MAP.put("ifm", "application/vnd.shana.informed.formdata");
        MIME_MAP.put("iges", "model/iges");
        MIME_MAP.put("igl", "application/vnd.igloader");
        MIME_MAP.put("igm", "application/vnd.insors.igm");
        MIME_MAP.put("igs", "model/iges");
        MIME_MAP.put("igx", "application/vnd.micrografx.igx");
        MIME_MAP.put("iif", "application/vnd.shana.informed.interchange");
        MIME_MAP.put("imp", "application/vnd.accpac.simply.imp");
        MIME_MAP.put("ims", "application/vnd.ms-ims");
        MIME_MAP.put("in", "text/plain");
        MIME_MAP.put("ink", "application/inkml+xml");
        MIME_MAP.put("inkml", "application/inkml+xml");
        MIME_MAP.put("install", "application/x-install-instructions");
        MIME_MAP.put("iota", "application/vnd.astraea-software.iota");
        MIME_MAP.put("ipfix", "application/ipfix");
        MIME_MAP.put("ipk", "application/vnd.shana.informed.package");
        MIME_MAP.put("irm", "application/vnd.ibm.rights-management");
        MIME_MAP.put("irp", "application/vnd.irepository.package+xml");
        MIME_MAP.put("iso", "application/x-iso9660-image");
        MIME_MAP.put("itp", "application/vnd.shana.informed.formtemplate");
        MIME_MAP.put("ivp", "application/vnd.immervision-ivp");
        MIME_MAP.put("ivu", "application/vnd.immervision-ivu");
        MIME_MAP.put("jad", "text/vnd.sun.j2me.app-descriptor");
        MIME_MAP.put("jam", "application/vnd.jam");
        MIME_MAP.put("jar", "application/java-archive");
        MIME_MAP.put("java", "text/x-java-source");
        MIME_MAP.put("jisp", "application/vnd.jisp");
        MIME_MAP.put("jlt", "application/vnd.hp-jlyt");
        MIME_MAP.put("jnlp", "application/x-java-jnlp-file");
        MIME_MAP.put("joda", "application/vnd.joost.joda-archive");
        MIME_MAP.put("jpe", "image/jpeg");
        MIME_MAP.put("jpeg", "image/jpeg");
        MIME_MAP.put("jpg", "image/jpeg");
        MIME_MAP.put("jpgm", "video/jpm");
        MIME_MAP.put("jpgv", "video/jpeg");
        MIME_MAP.put("jpm", "video/jpm");
        MIME_MAP.put("js", "application/javascript");
        MIME_MAP.put("jsf", "text/plain");
        MIME_MAP.put("json", "application/json");
        MIME_MAP.put("jsonml", "application/jsonml+json");
        MIME_MAP.put("jspf", "text/plain");
        MIME_MAP.put("kar", "audio/midi");
        MIME_MAP.put("karbon", "application/vnd.kde.karbon");
        MIME_MAP.put("kfo", "application/vnd.kde.kformula");
        MIME_MAP.put("kia", "application/vnd.kidspiration");
        MIME_MAP.put("kml", "application/vnd.google-earth.kml+xml");
        MIME_MAP.put("kmz", "application/vnd.google-earth.kmz");
        MIME_MAP.put("kne", "application/vnd.kinar");
        MIME_MAP.put("knp", "application/vnd.kinar");
        MIME_MAP.put("kon", "application/vnd.kde.kontour");
        MIME_MAP.put("kpr", "application/vnd.kde.kpresenter");
        MIME_MAP.put("kpt", "application/vnd.kde.kpresenter");
        MIME_MAP.put("kpxx", "application/vnd.ds-keypoint");
        MIME_MAP.put("ksp", "application/vnd.kde.kspread");
        MIME_MAP.put("ktr", "application/vnd.kahootz");
        MIME_MAP.put("ktx", "image/ktx");
        MIME_MAP.put("ktz", "application/vnd.kahootz");
        MIME_MAP.put("kwd", "application/vnd.kde.kword");
        MIME_MAP.put("kwt", "application/vnd.kde.kword");
        MIME_MAP.put("lasxml", "application/vnd.las.las+xml");
        MIME_MAP.put("latex", "application/x-latex");
        MIME_MAP.put("lbd", "application/vnd.llamagraphics.life-balance.desktop");
        MIME_MAP.put("lbe", "application/vnd.llamagraphics.life-balance.exchange+xml");
        MIME_MAP.put("les", "application/vnd.hhe.lesson-player");
        MIME_MAP.put("lha", "application/x-lzh-compressed");
        MIME_MAP.put("link66", "application/vnd.route66.link66+xml");
        MIME_MAP.put("list", "text/plain");
        MIME_MAP.put("list3820", "application/vnd.ibm.modcap");
        MIME_MAP.put("listafp", "application/vnd.ibm.modcap");
        MIME_MAP.put("lnk", "application/x-ms-shortcut");
        MIME_MAP.put("log", "text/plain");
        MIME_MAP.put("lostxml", "application/lost+xml");
        MIME_MAP.put("lrf", "application/octet-stream");
        MIME_MAP.put("lrm", "application/vnd.ms-lrm");
        MIME_MAP.put("ltf", "application/vnd.frogans.ltf");
        MIME_MAP.put("lvp", "audio/vnd.lucent.voice");
        MIME_MAP.put("lwp", "application/vnd.lotus-wordpro");
        MIME_MAP.put("lzh", "application/x-lzh-compressed");
        MIME_MAP.put("m13", "application/x-msmediaview");
        MIME_MAP.put("m14", "application/x-msmediaview");
        MIME_MAP.put("m1v", "video/mpeg");
        MIME_MAP.put("m21", "application/mp21");
        MIME_MAP.put("m2a", "audio/mpeg");
        MIME_MAP.put("m2v", "video/mpeg");
        MIME_MAP.put("m3a", "audio/mpeg");
        MIME_MAP.put("m3u", "audio/x-mpegurl");
        MIME_MAP.put("m3u8", "application/vnd.apple.mpegurl");
        MIME_MAP.put("m4a", "audio/mp4");
        MIME_MAP.put("m4b", "audio/mp4");
        MIME_MAP.put("m4r", "audio/mp4");
        MIME_MAP.put("m4u", "video/vnd.mpegurl");
        MIME_MAP.put("m4v", "video/mp4");
        MIME_MAP.put("ma", "application/mathematica");
        MIME_MAP.put("mac", "image/x-macpaint");
        MIME_MAP.put("mads", "application/mads+xml");
        MIME_MAP.put("mag", "application/vnd.ecowin.chart");
        MIME_MAP.put("maker", "application/vnd.framemaker");
        MIME_MAP.put("man", "text/troff");
        MIME_MAP.put("mar", "application/octet-stream");
        MIME_MAP.put("mathml", "application/mathml+xml");
        MIME_MAP.put("mb", "application/mathematica");
        MIME_MAP.put("mbk", "application/vnd.mobius.mbk");
        MIME_MAP.put("mbox", "application/mbox");
        MIME_MAP.put("mc1", "application/vnd.medcalcdata");
        MIME_MAP.put("mcd", "application/vnd.mcd");
        MIME_MAP.put("mcurl", "text/vnd.curl.mcurl");
        MIME_MAP.put("mdb", "application/x-msaccess");
        MIME_MAP.put("mdi", "image/vnd.ms-modi");
        MIME_MAP.put("me", "text/troff");
        MIME_MAP.put("mesh", "model/mesh");
        MIME_MAP.put("meta4", "application/metalink4+xml");
        MIME_MAP.put("metalink", "application/metalink+xml");
        MIME_MAP.put("mets", "application/mets+xml");
        MIME_MAP.put("mfm", "application/vnd.mfmp");
        MIME_MAP.put("mft", "application/rpki-manifest");
        MIME_MAP.put("mgp", "application/vnd.osgeo.mapguide.package");
        MIME_MAP.put("mgz", "application/vnd.proteus.magazine");
        MIME_MAP.put("mid", "audio/midi");
        MIME_MAP.put("midi", "audio/midi");
        MIME_MAP.put("mie", "application/x-mie");
        MIME_MAP.put("mif", "application/x-mif");
        MIME_MAP.put("mime", "message/rfc822");
        MIME_MAP.put("mj2", "video/mj2");
        MIME_MAP.put("mjp2", "video/mj2");
        MIME_MAP.put("mk3d", "video/x-matroska");
        MIME_MAP.put("mka", "audio/x-matroska");
        MIME_MAP.put("mks", "video/x-matroska");
        MIME_MAP.put("mkv", "video/x-matroska");
        MIME_MAP.put("mlp", "application/vnd.dolby.mlp");
        MIME_MAP.put("mmd", "application/vnd.chipnuts.karaoke-mmd");
        MIME_MAP.put("mmf", "application/vnd.smaf");
        MIME_MAP.put("mmr", "image/vnd.fujixerox.edmics-mmr");
        MIME_MAP.put("mng", "video/x-mng");
        MIME_MAP.put("mny", "application/x-msmoney");
        MIME_MAP.put("mobi", "application/x-mobipocket-ebook");
        MIME_MAP.put("mods", "application/mods+xml");
        MIME_MAP.put("mov", "video/quicktime");
        MIME_MAP.put("movie", "video/x-sgi-movie");
        MIME_MAP.put("mp1", "audio/mpeg");
        MIME_MAP.put("mp2", "audio/mpeg");
        MIME_MAP.put("mp21", "application/mp21");
        MIME_MAP.put("mp2a", "audio/mpeg");
        MIME_MAP.put("mp3", "audio/mpeg");
        MIME_MAP.put("mp4", "video/mp4");
        MIME_MAP.put("mp4a", "audio/mp4");
        MIME_MAP.put("mp4s", "application/mp4");
        MIME_MAP.put("mp4v", "video/mp4");
        MIME_MAP.put("mpa", "audio/mpeg");
        MIME_MAP.put("mpc", "application/vnd.mophun.certificate");
        MIME_MAP.put("mpe", "video/mpeg");
        MIME_MAP.put("mpeg", "video/mpeg");
        MIME_MAP.put("mpega", "audio/x-mpeg");
        MIME_MAP.put("mpg", "video/mpeg");
        MIME_MAP.put("mpg4", "video/mp4");
        MIME_MAP.put("mpga", "audio/mpeg");
        MIME_MAP.put("mpkg", "application/vnd.apple.installer+xml");
        MIME_MAP.put("mpm", "application/vnd.blueice.multipass");
        MIME_MAP.put("mpn", "application/vnd.mophun.application");
        MIME_MAP.put("mpp", "application/vnd.ms-project");
        MIME_MAP.put("mpt", "application/vnd.ms-project");
        MIME_MAP.put("mpv2", "video/mpeg2");
        MIME_MAP.put("mpy", "application/vnd.ibm.minipay");
        MIME_MAP.put("mqy", "application/vnd.mobius.mqy");
        MIME_MAP.put("mrc", "application/marc");
        MIME_MAP.put("mrcx", "application/marcxml+xml");
        MIME_MAP.put("ms", "text/troff");
        MIME_MAP.put("mscml", "application/mediaservercontrol+xml");
        MIME_MAP.put("mseed", "application/vnd.fdsn.mseed");
        MIME_MAP.put("mseq", "application/vnd.mseq");
        MIME_MAP.put("msf", "application/vnd.epson.msf");
        MIME_MAP.put("msh", "model/mesh");
        MIME_MAP.put("msi", "application/x-msdownload");
        MIME_MAP.put("msl", "application/vnd.mobius.msl");
        MIME_MAP.put("msty", "application/vnd.muvee.style");
        MIME_MAP.put("mts", "model/vnd.mts");
        MIME_MAP.put("mus", "application/vnd.musician");
        MIME_MAP.put("musicxml", "application/vnd.recordare.musicxml+xml");
        MIME_MAP.put("mvb", "application/x-msmediaview");
        MIME_MAP.put("mwf", "application/vnd.mfer");
        MIME_MAP.put("mxf", "application/mxf");
        MIME_MAP.put("mxl", "application/vnd.recordare.musicxml");
        MIME_MAP.put("mxml", "application/xv+xml");
        MIME_MAP.put("mxs", "application/vnd.triscape.mxs");
        MIME_MAP.put("mxu", "video/vnd.mpegurl");
        MIME_MAP.put("n-gage", "application/vnd.nokia.n-gage.symbian.install");
        MIME_MAP.put("n3", "text/n3");
        MIME_MAP.put("nb", "application/mathematica");
        MIME_MAP.put("nbp", "application/vnd.wolfram.player");
        MIME_MAP.put("nc", "application/x-netcdf");
        MIME_MAP.put("ncx", "application/x-dtbncx+xml");
        MIME_MAP.put("nfo", "text/x-nfo");
        MIME_MAP.put("ngdat", "application/vnd.nokia.n-gage.data");
        MIME_MAP.put("nitf", "application/vnd.nitf");
        MIME_MAP.put("nlu", "application/vnd.neurolanguage.nlu");
        MIME_MAP.put("nml", "application/vnd.enliven");
        MIME_MAP.put("nnd", "application/vnd.noblenet-directory");
        MIME_MAP.put("nns", "application/vnd.noblenet-sealer");
        MIME_MAP.put("nnw", "application/vnd.noblenet-web");
        MIME_MAP.put("npx", "image/vnd.net-fpx");
        MIME_MAP.put("nsc", "application/x-conference");
        MIME_MAP.put("nsf", "application/vnd.lotus-notes");
        MIME_MAP.put("ntf", "application/vnd.nitf");
        MIME_MAP.put("nzb", "application/x-nzb");
        MIME_MAP.put("oa2", "application/vnd.fujitsu.oasys2");
        MIME_MAP.put("oa3", "application/vnd.fujitsu.oasys3");
        MIME_MAP.put("oas", "application/vnd.fujitsu.oasys");
        MIME_MAP.put("obd", "application/x-msbinder");
        MIME_MAP.put("obj", "application/x-tgif");
        MIME_MAP.put("oda", "application/oda");
        MIME_MAP.put("odb", "application/vnd.oasis.opendocument.database");
        MIME_MAP.put("odc", "application/vnd.oasis.opendocument.chart");
        MIME_MAP.put("odf", "application/vnd.oasis.opendocument.formula");
        MIME_MAP.put("odft", "application/vnd.oasis.opendocument.formula-template");
        MIME_MAP.put("odg", "application/vnd.oasis.opendocument.graphics");
        MIME_MAP.put("odi", "application/vnd.oasis.opendocument.image");
        MIME_MAP.put("odm", "application/vnd.oasis.opendocument.text-master");
        MIME_MAP.put("odp", "application/vnd.oasis.opendocument.presentation");
        MIME_MAP.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
        MIME_MAP.put("odt", "application/vnd.oasis.opendocument.text");
        MIME_MAP.put("oga", "audio/ogg");
        MIME_MAP.put("ogg", "audio/ogg");
        MIME_MAP.put("ogv", "video/ogg");
        MIME_MAP.put("ogx", "application/ogg");
        MIME_MAP.put("omdoc", "application/omdoc+xml");
        MIME_MAP.put("onepkg", "application/onenote");
        MIME_MAP.put("onetmp", "application/onenote");
        MIME_MAP.put("onetoc", "application/onenote");
        MIME_MAP.put("onetoc2", "application/onenote");
        MIME_MAP.put("opf", "application/oebps-package+xml");
        MIME_MAP.put("opml", "text/x-opml");
        MIME_MAP.put("oprc", "application/vnd.palm");
        MIME_MAP.put("org", "application/vnd.lotus-organizer");
        MIME_MAP.put("osf", "application/vnd.yamaha.openscoreformat");
        MIME_MAP.put("osfpvg", "application/vnd.yamaha.openscoreformat.osfpvg+xml");
        MIME_MAP.put("otc", "application/vnd.oasis.opendocument.chart-template");
        MIME_MAP.put("otf", "application/x-font-otf");
        MIME_MAP.put("otg", "application/vnd.oasis.opendocument.graphics-template");
        MIME_MAP.put("oth", "application/vnd.oasis.opendocument.text-web");
        MIME_MAP.put("oti", "application/vnd.oasis.opendocument.image-template");
        MIME_MAP.put("otp", "application/vnd.oasis.opendocument.presentation-template");
        MIME_MAP.put("ots", "application/vnd.oasis.opendocument.spreadsheet-template");
        MIME_MAP.put("ott", "application/vnd.oasis.opendocument.text-template");
        MIME_MAP.put("oxps", "application/oxps");
        MIME_MAP.put("oxt", "application/vnd.openofficeorg.extension");
        MIME_MAP.put("p", "text/x-pascal");
        MIME_MAP.put("p10", "application/pkcs10");
        MIME_MAP.put("p12", "application/x-pkcs12");
        MIME_MAP.put("p7b", "application/x-pkcs7-certificates");
        MIME_MAP.put("p7c", "application/pkcs7-mime");
        MIME_MAP.put("p7m", "application/pkcs7-mime");
        MIME_MAP.put("p7r", "application/x-pkcs7-certreqresp");
        MIME_MAP.put("p7s", "application/pkcs7-signature");
        MIME_MAP.put("p8", "application/pkcs8");
        MIME_MAP.put("pas", "text/x-pascal");
        MIME_MAP.put("paw", "application/vnd.pawaafile");
        MIME_MAP.put("pbd", "application/vnd.powerbuilder6");
        MIME_MAP.put("pbm", "image/x-portable-bitmap");
        MIME_MAP.put("pcap", "application/vnd.tcpdump.pcap");
        MIME_MAP.put("pcf", "application/x-font-pcf");
        MIME_MAP.put("pcl", "application/vnd.hp-pcl");
        MIME_MAP.put("pclxl", "application/vnd.hp-pclxl");
        MIME_MAP.put("pct", "image/pict");
        MIME_MAP.put("pcurl", "application/vnd.curl.pcurl");
        MIME_MAP.put("pcx", "image/x-pcx");
        MIME_MAP.put("pdb", "application/vnd.palm");
        MIME_MAP.put("pdf", "application/pdf");
        MIME_MAP.put("pfa", "application/x-font-type1");
        MIME_MAP.put("pfb", "application/x-font-type1");
        MIME_MAP.put("pfm", "application/x-font-type1");
        MIME_MAP.put("pfr", "application/font-tdpfr");
        MIME_MAP.put("pfx", "application/x-pkcs12");
        MIME_MAP.put("pgm", "image/x-portable-graymap");
        MIME_MAP.put("pgn", "application/x-chess-pgn");
        MIME_MAP.put("pgp", "application/pgp-encrypted");
        MIME_MAP.put("pic", "image/pict");
        MIME_MAP.put("pict", "image/pict");
        MIME_MAP.put("pkg", "application/octet-stream");
        MIME_MAP.put("pki", "application/pkixcmp");
        MIME_MAP.put("pkipath", "application/pkix-pkipath");
        MIME_MAP.put("plb", "application/vnd.3gpp.pic-bw-large");
        MIME_MAP.put("plc", "application/vnd.mobius.plc");
        MIME_MAP.put("plf", "application/vnd.pocketlearn");
        MIME_MAP.put("pls", "audio/x-scpls");
        MIME_MAP.put("pml", "application/vnd.ctc-posml");
        MIME_MAP.put("png", "image/png");
        MIME_MAP.put("pnm", "image/x-portable-anymap");
        MIME_MAP.put("pnt", "image/x-macpaint");
        MIME_MAP.put("portpkg", "application/vnd.macports.portpkg");
        MIME_MAP.put("pot", "application/vnd.ms-powerpoint");
        MIME_MAP.put("potm", "application/vnd.ms-powerpoint.template.macroenabled.12");
        MIME_MAP.put("potx", "application/vnd.openxmlformats-officedocument.presentationml.template");
        MIME_MAP.put("ppam", "application/vnd.ms-powerpoint.addin.macroenabled.12");
        MIME_MAP.put("ppd", "application/vnd.cups-ppd");
        MIME_MAP.put("ppm", "image/x-portable-pixmap");
        MIME_MAP.put("pps", "application/vnd.ms-powerpoint");
        MIME_MAP.put("ppsm", "application/vnd.ms-powerpoint.slideshow.macroenabled.12");
        MIME_MAP.put("ppsx", "application/vnd.openxmlformats-officedocument.presentationml.slideshow");
        MIME_MAP.put("ppt", "application/vnd.ms-powerpoint");
        MIME_MAP.put("pptm", "application/vnd.ms-powerpoint.presentation.macroenabled.12");
        MIME_MAP.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        MIME_MAP.put("pqa", "application/vnd.palm");
        MIME_MAP.put("prc", "application/x-mobipocket-ebook");
        MIME_MAP.put("pre", "application/vnd.lotus-freelance");
        MIME_MAP.put("prf", "application/pics-rules");
        MIME_MAP.put("ps", "application/postscript");
        MIME_MAP.put("psb", "application/vnd.3gpp.pic-bw-small");
        MIME_MAP.put("psd", "image/vnd.adobe.photoshop");
        MIME_MAP.put("psf", "application/x-font-linux-psf");
        MIME_MAP.put("pskcxml", "application/pskc+xml");
        MIME_MAP.put("ptid", "application/vnd.pvi.ptid1");
        MIME_MAP.put("pub", "application/x-mspublisher");
        MIME_MAP.put("pvb", "application/vnd.3gpp.pic-bw-var");
        MIME_MAP.put("pwn", "application/vnd.3m.post-it-notes");
        MIME_MAP.put("pya", "audio/vnd.ms-playready.media.pya");
        MIME_MAP.put("pyv", "video/vnd.ms-playready.media.pyv");
        MIME_MAP.put("qam", "application/vnd.epson.quickanime");
        MIME_MAP.put("qbo", "application/vnd.intu.qbo");
        MIME_MAP.put("qfx", "application/vnd.intu.qfx");
        MIME_MAP.put("qps", "application/vnd.publishare-delta-tree");
        MIME_MAP.put("qt", "video/quicktime");
        MIME_MAP.put("qti", "image/x-quicktime");
        MIME_MAP.put("qtif", "image/x-quicktime");
        MIME_MAP.put("qwd", "application/vnd.quark.quarkxpress");
        MIME_MAP.put("qwt", "application/vnd.quark.quarkxpress");
        MIME_MAP.put("qxb", "application/vnd.quark.quarkxpress");
        MIME_MAP.put("qxd", "application/vnd.quark.quarkxpress");
        MIME_MAP.put("qxl", "application/vnd.quark.quarkxpress");
        MIME_MAP.put("qxt", "application/vnd.quark.quarkxpress");
        MIME_MAP.put("ra", "audio/x-pn-realaudio");
        MIME_MAP.put("ram", "audio/x-pn-realaudio");
        MIME_MAP.put("rar", "application/x-rar-compressed");
        MIME_MAP.put("ras", "image/x-cmu-raster");
        MIME_MAP.put("rcprofile", "application/vnd.ipunplugged.rcprofile");
        MIME_MAP.put("rdf", "application/rdf+xml");
        MIME_MAP.put("rdz", "application/vnd.data-vision.rdz");
        MIME_MAP.put("rep", "application/vnd.businessobjects");
        MIME_MAP.put("res", "application/x-dtbresource+xml");
        MIME_MAP.put("rgb", "image/x-rgb");
        MIME_MAP.put("rif", "application/reginfo+xml");
        MIME_MAP.put("rip", "audio/vnd.rip");
        MIME_MAP.put("ris", "application/x-research-info-systems");
        MIME_MAP.put("rl", "application/resource-lists+xml");
        MIME_MAP.put("rlc", "image/vnd.fujixerox.edmics-rlc");
        MIME_MAP.put("rld", "application/resource-lists-diff+xml");
        MIME_MAP.put("rm", "application/vnd.rn-realmedia");
        MIME_MAP.put("rmi", "audio/midi");
        MIME_MAP.put("rmp", "audio/x-pn-realaudio-plugin");
        MIME_MAP.put("rms", "application/vnd.jcp.javame.midlet-rms");
        MIME_MAP.put("rmvb", "application/vnd.rn-realmedia-vbr");
        MIME_MAP.put("rnc", "application/relax-ng-compact-syntax");
        MIME_MAP.put("roa", "application/rpki-roa");
        MIME_MAP.put("roff", "text/troff");
        MIME_MAP.put("rp9", "application/vnd.cloanto.rp9");
        MIME_MAP.put("rpss", "application/vnd.nokia.radio-presets");
        MIME_MAP.put("rpst", "application/vnd.nokia.radio-preset");
        MIME_MAP.put("rq", "application/sparql-query");
        MIME_MAP.put("rs", "application/rls-services+xml");
        MIME_MAP.put("rsd", "application/rsd+xml");
        MIME_MAP.put("rss", "application/rss+xml");
        MIME_MAP.put("rtf", "application/rtf");
        MIME_MAP.put("rtx", "text/richtext");
        MIME_MAP.put("s", "text/x-asm");
        MIME_MAP.put("s3m", "audio/s3m");
        MIME_MAP.put("saf", "application/vnd.yamaha.smaf-audio");
        MIME_MAP.put("sbml", "application/sbml+xml");
        MIME_MAP.put("sc", "application/vnd.ibm.secure-container");
        MIME_MAP.put("scd", "application/x-msschedule");
        MIME_MAP.put("scm", "application/vnd.lotus-screencam");
        MIME_MAP.put("scq", "application/scvp-cv-request");
        MIME_MAP.put("scs", "application/scvp-cv-response");
        MIME_MAP.put("scurl", "text/vnd.curl.scurl");
        MIME_MAP.put("sda", "application/vnd.stardivision.draw");
        MIME_MAP.put("sdc", "application/vnd.stardivision.calc");
        MIME_MAP.put("sdd", "application/vnd.stardivision.impress");
        MIME_MAP.put("sdkd", "application/vnd.solent.sdkm+xml");
        MIME_MAP.put("sdkm", "application/vnd.solent.sdkm+xml");
        MIME_MAP.put("sdp", "application/sdp");
        MIME_MAP.put("sdw", "application/vnd.stardivision.writer");
        MIME_MAP.put("see", "application/vnd.seemail");
        MIME_MAP.put("seed", "application/vnd.fdsn.seed");
        MIME_MAP.put("sema", "application/vnd.sema");
        MIME_MAP.put("semd", "application/vnd.semd");
        MIME_MAP.put("semf", "application/vnd.semf");
        MIME_MAP.put("ser", "application/java-serialized-object");
        MIME_MAP.put("setpay", "application/set-payment-initiation");
        MIME_MAP.put("setreg", "application/set-registration-initiation");
        MIME_MAP.put("sfd-hdstx", "application/vnd.hydrostatix.sof-data");
        MIME_MAP.put("sfs", "application/vnd.spotfire.sfs");
        MIME_MAP.put("sfv", "text/x-sfv");
        MIME_MAP.put("sgi", "image/sgi");
        MIME_MAP.put("sgl", "application/vnd.stardivision.writer-global");
        MIME_MAP.put("sgm", "text/sgml");
        MIME_MAP.put("sgml", "text/sgml");
        MIME_MAP.put("sh", "application/x-sh");
        MIME_MAP.put("shar", "application/x-shar");
        MIME_MAP.put("shf", "application/shf+xml");
        MIME_MAP.put("shtml", "text/x-server-parsed-html");
        MIME_MAP.put("sid", "image/x-mrsid-image");
        MIME_MAP.put("sig", "application/pgp-signature");
        MIME_MAP.put("sil", "audio/silk");
        MIME_MAP.put("silo", "model/mesh");
        MIME_MAP.put("sis", "application/vnd.symbian.install");
        MIME_MAP.put("sisx", "application/vnd.symbian.install");
        MIME_MAP.put("sit", "application/x-stuffit");
        MIME_MAP.put("sitx", "application/x-stuffitx");
        MIME_MAP.put("skd", "application/vnd.koan");
        MIME_MAP.put("skm", "application/vnd.koan");
        MIME_MAP.put("skp", "application/vnd.koan");
        MIME_MAP.put("skt", "application/vnd.koan");
        MIME_MAP.put("sldm", "application/vnd.ms-powerpoint.slide.macroenabled.12");
        MIME_MAP.put("sldx", "application/vnd.openxmlformats-officedocument.presentationml.slide");
        MIME_MAP.put("slt", "application/vnd.epson.salt");
        MIME_MAP.put("sm", "application/vnd.stepmania.stepchart");
        MIME_MAP.put("smf", "application/vnd.stardivision.math");
        MIME_MAP.put("smi", "application/smil+xml");
        MIME_MAP.put("smil", "application/smil+xml");
        MIME_MAP.put("smv", "video/x-smv");
        MIME_MAP.put("smzip", "application/vnd.stepmania.package");
        MIME_MAP.put("snd", "audio/basic");
        MIME_MAP.put("snf", "application/x-font-snf");
        MIME_MAP.put("so", "application/octet-stream");
        MIME_MAP.put("spc", "application/x-pkcs7-certificates");
        MIME_MAP.put("spf", "application/vnd.yamaha.smaf-phrase");
        MIME_MAP.put("spl", "application/x-futuresplash");
        MIME_MAP.put("spot", "text/vnd.in3d.spot");
        MIME_MAP.put("spp", "application/scvp-vp-response");
        MIME_MAP.put("spq", "application/scvp-vp-request");
        MIME_MAP.put("spx", "audio/ogg");
        MIME_MAP.put("sql", "application/x-sql");
        MIME_MAP.put("src", "application/x-wais-source");
        MIME_MAP.put("srt", "application/x-subrip");
        MIME_MAP.put("sru", "application/sru+xml");
        MIME_MAP.put("srx", "application/sparql-results+xml");
        MIME_MAP.put("ssdl", "application/ssdl+xml");
        MIME_MAP.put("sse", "application/vnd.kodak-descriptor");
        MIME_MAP.put("ssf", "application/vnd.epson.ssf");
        MIME_MAP.put("ssml", "application/ssml+xml");
        MIME_MAP.put("st", "application/vnd.sailingtracker.track");
        MIME_MAP.put("stc", "application/vnd.sun.xml.calc.template");
        MIME_MAP.put("std", "application/vnd.sun.xml.draw.template");
        MIME_MAP.put("stf", "application/vnd.wt.stf");
        MIME_MAP.put("sti", "application/vnd.sun.xml.impress.template");
        MIME_MAP.put("stk", "application/hyperstudio");
        MIME_MAP.put("stl", "application/vnd.ms-pki.stl");
        MIME_MAP.put("str", "application/vnd.pg.format");
        MIME_MAP.put("stw", "application/vnd.sun.xml.writer.template");
        MIME_MAP.put("sub", "text/vnd.dvb.subtitle");
        MIME_MAP.put("sus", "application/vnd.sus-calendar");
        MIME_MAP.put("susp", "application/vnd.sus-calendar");
        MIME_MAP.put("sv4cpio", "application/x-sv4cpio");
        MIME_MAP.put("sv4crc", "application/x-sv4crc");
        MIME_MAP.put("svc", "application/vnd.dvb.service");
        MIME_MAP.put("svd", "application/vnd.svd");
        MIME_MAP.put("svg", "image/svg+xml");
        MIME_MAP.put("svgz", "image/svg+xml");
        MIME_MAP.put("swa", "application/x-director");
        MIME_MAP.put("swf", "application/x-shockwave-flash");
        MIME_MAP.put("swi", "application/vnd.aristanetworks.swi");
        MIME_MAP.put("sxc", "application/vnd.sun.xml.calc");
        MIME_MAP.put("sxd", "application/vnd.sun.xml.draw");
        MIME_MAP.put("sxg", "application/vnd.sun.xml.writer.global");
        MIME_MAP.put("sxi", "application/vnd.sun.xml.impress");
        MIME_MAP.put("sxm", "application/vnd.sun.xml.math");
        MIME_MAP.put("sxw", "application/vnd.sun.xml.writer");
        MIME_MAP.put("t", "text/troff");
        MIME_MAP.put("t3", "application/x-t3vm-image");
        MIME_MAP.put("taglet", "application/vnd.mynfc");
        MIME_MAP.put("tao", "application/vnd.tao.intent-module-archive");
        MIME_MAP.put("tar", "application/x-tar");
        MIME_MAP.put("tcap", "application/vnd.3gpp2.tcap");
        MIME_MAP.put("tcl", "application/x-tcl");
        MIME_MAP.put("teacher", "application/vnd.smart.teacher");
        MIME_MAP.put("tei", "application/tei+xml");
        MIME_MAP.put("teicorpus", "application/tei+xml");
        MIME_MAP.put("tex", "application/x-tex");
        MIME_MAP.put("texi", "application/x-texinfo");
        MIME_MAP.put("texinfo", "application/x-texinfo");
        MIME_MAP.put("text", "text/plain");
        MIME_MAP.put("tfi", "application/thraud+xml");
        MIME_MAP.put("tfm", "application/x-tex-tfm");
        MIME_MAP.put("tga", "image/x-tga");
        MIME_MAP.put("thmx", "application/vnd.ms-officetheme");
        MIME_MAP.put("tif", "image/tiff");
        MIME_MAP.put("tiff", "image/tiff");
        MIME_MAP.put("tmo", "application/vnd.tmobile-livetv");
        MIME_MAP.put("torrent", "application/x-bittorrent");
        MIME_MAP.put("tpl", "application/vnd.groove-tool-template");
        MIME_MAP.put("tpt", "application/vnd.trid.tpt");
        MIME_MAP.put("tr", "text/troff");
        MIME_MAP.put("tra", "application/vnd.trueapp");
        MIME_MAP.put("trm", "application/x-msterminal");
        MIME_MAP.put("tsd", "application/timestamped-data");
        MIME_MAP.put("tsv", "text/tab-separated-values");
        MIME_MAP.put("ttc", "application/x-font-ttf");
        MIME_MAP.put("ttf", "application/x-font-ttf");
        MIME_MAP.put("ttl", "text/turtle");
        MIME_MAP.put("twd", "application/vnd.simtech-mindmapper");
        MIME_MAP.put("twds", "application/vnd.simtech-mindmapper");
        MIME_MAP.put("txd", "application/vnd.genomatix.tuxedo");
        MIME_MAP.put("txf", "application/vnd.mobius.txf");
        MIME_MAP.put("txt", "text/plain");
        MIME_MAP.put("u32", "application/x-authorware-bin");
        MIME_MAP.put("udeb", "application/x-debian-package");
        MIME_MAP.put("ufd", "application/vnd.ufdl");
        MIME_MAP.put("ufdl", "application/vnd.ufdl");
        MIME_MAP.put("ulw", "audio/basic");
        MIME_MAP.put("ulx", "application/x-glulx");
        MIME_MAP.put("umj", "application/vnd.umajin");
        MIME_MAP.put("unityweb", "application/vnd.unity");
        MIME_MAP.put("uoml", "application/vnd.uoml+xml");
        MIME_MAP.put("uri", "text/uri-list");
        MIME_MAP.put("uris", "text/uri-list");
        MIME_MAP.put("urls", "text/uri-list");
        MIME_MAP.put("ustar", "application/x-ustar");
        MIME_MAP.put("utz", "application/vnd.uiq.theme");
        MIME_MAP.put("uu", "text/x-uuencode");
        MIME_MAP.put("uva", "audio/vnd.dece.audio");
        MIME_MAP.put("uvd", "application/vnd.dece.data");
        MIME_MAP.put("uvf", "application/vnd.dece.data");
        MIME_MAP.put("uvg", "image/vnd.dece.graphic");
        MIME_MAP.put("uvh", "video/vnd.dece.hd");
        MIME_MAP.put("uvi", "image/vnd.dece.graphic");
        MIME_MAP.put("uvm", "video/vnd.dece.mobile");
        MIME_MAP.put("uvp", "video/vnd.dece.pd");
        MIME_MAP.put("uvs", "video/vnd.dece.sd");
        MIME_MAP.put("uvt", "application/vnd.dece.ttml+xml");
        MIME_MAP.put("uvu", "video/vnd.uvvu.mp4");
        MIME_MAP.put("uvv", "video/vnd.dece.video");
        MIME_MAP.put("uvva", "audio/vnd.dece.audio");
        MIME_MAP.put("uvvd", "application/vnd.dece.data");
        MIME_MAP.put("uvvf", "application/vnd.dece.data");
        MIME_MAP.put("uvvg", "image/vnd.dece.graphic");
        MIME_MAP.put("uvvh", "video/vnd.dece.hd");
        MIME_MAP.put("uvvi", "image/vnd.dece.graphic");
        MIME_MAP.put("uvvm", "video/vnd.dece.mobile");
        MIME_MAP.put("uvvp", "video/vnd.dece.pd");
        MIME_MAP.put("uvvs", "video/vnd.dece.sd");
        MIME_MAP.put("uvvt", "application/vnd.dece.ttml+xml");
        MIME_MAP.put("uvvu", "video/vnd.uvvu.mp4");
        MIME_MAP.put("uvvv", "video/vnd.dece.video");
        MIME_MAP.put("uvvx", "application/vnd.dece.unspecified");
        MIME_MAP.put("uvvz", "application/vnd.dece.zip");
        MIME_MAP.put("uvx", "application/vnd.dece.unspecified");
        MIME_MAP.put("uvz", "application/vnd.dece.zip");
        MIME_MAP.put("vcard", "text/vcard");
        MIME_MAP.put("vcd", "application/x-cdlink");
        MIME_MAP.put("vcf", "text/x-vcard");
        MIME_MAP.put("vcg", "application/vnd.groove-vcard");
        MIME_MAP.put("vcs", "text/x-vcalendar");
        MIME_MAP.put("vcx", "application/vnd.vcx");
        MIME_MAP.put("vis", "application/vnd.visionary");
        MIME_MAP.put("viv", "video/vnd.vivo");
        MIME_MAP.put("vob", "video/x-ms-vob");
        MIME_MAP.put("vor", "application/vnd.stardivision.writer");
        MIME_MAP.put("vox", "application/x-authorware-bin");
        MIME_MAP.put("vrml", "model/vrml");
        MIME_MAP.put("vsd", "application/vnd.visio");
        MIME_MAP.put("vsf", "application/vnd.vsf");
        MIME_MAP.put("vss", "application/vnd.visio");
        MIME_MAP.put("vst", "application/vnd.visio");
        MIME_MAP.put("vsw", "application/vnd.visio");
        MIME_MAP.put("vtu", "model/vnd.vtu");
        MIME_MAP.put("vxml", "application/voicexml+xml");
        MIME_MAP.put("w3d", "application/x-director");
        MIME_MAP.put("wad", "application/x-doom");
        MIME_MAP.put("wav", "audio/x-wav");
        MIME_MAP.put("wax", "audio/x-ms-wax");
        MIME_MAP.put("wbmp", "image/vnd.wap.wbmp");
        MIME_MAP.put("wbs", "application/vnd.criticaltools.wbs+xml");
        MIME_MAP.put("wbxml", "application/vnd.wap.wbxml");
        MIME_MAP.put("wcm", "application/vnd.ms-works");
        MIME_MAP.put("wdb", "application/vnd.ms-works");
        MIME_MAP.put("wdp", "image/vnd.ms-photo");
        MIME_MAP.put("weba", "audio/webm");
        MIME_MAP.put("webm", "video/webm");
        MIME_MAP.put("webp", "image/webp");
        MIME_MAP.put("wg", "application/vnd.pmi.widget");
        MIME_MAP.put("wgt", "application/widget");
        MIME_MAP.put("wks", "application/vnd.ms-works");
        MIME_MAP.put("wm", "video/x-ms-wm");
        MIME_MAP.put("wma", "audio/x-ms-wma");
        MIME_MAP.put("wmd", "application/x-ms-wmd");
        MIME_MAP.put("wmf", "application/x-msmetafile");
        MIME_MAP.put("wml", "text/vnd.wap.wml");
        MIME_MAP.put("wmlc", "application/vnd.wap.wmlc");
        MIME_MAP.put("wmls", "text/vnd.wap.wmlscript");
        MIME_MAP.put("wmlsc", "application/vnd.wap.wmlscriptc");
        MIME_MAP.put("wmv", "video/x-ms-wmv");
        MIME_MAP.put("wmx", "video/x-ms-wmx");
        MIME_MAP.put("wmz", "application/x-msmetafile");
        MIME_MAP.put("woff", "application/x-font-woff");
        MIME_MAP.put("wpd", "application/vnd.wordperfect");
        MIME_MAP.put("wpl", "application/vnd.ms-wpl");
        MIME_MAP.put("wps", "application/vnd.ms-works");
        MIME_MAP.put("wqd", "application/vnd.wqd");
        MIME_MAP.put("wri", "application/x-mswrite");
        MIME_MAP.put("wrl", "model/vrml");
        MIME_MAP.put("wsdl", "application/wsdl+xml");
        MIME_MAP.put("wspolicy", "application/wspolicy+xml");
        MIME_MAP.put("wtb", "application/vnd.webturbo");
        MIME_MAP.put("wvx", "video/x-ms-wvx");
        MIME_MAP.put("x32", "application/x-authorware-bin");
        MIME_MAP.put("x3d", "model/x3d+xml");
        MIME_MAP.put("x3db", "model/x3d+binary");
        MIME_MAP.put("x3dbz", "model/x3d+binary");
        MIME_MAP.put("x3dv", "model/x3d+vrml");
        MIME_MAP.put("x3dvz", "model/x3d+vrml");
        MIME_MAP.put("x3dz", "model/x3d+xml");
        MIME_MAP.put("xaml", "application/xaml+xml");
        MIME_MAP.put("xap", "application/x-silverlight-app");
        MIME_MAP.put("xar", "application/vnd.xara");
        MIME_MAP.put("xbap", "application/x-ms-xbap");
        MIME_MAP.put("xbd", "application/vnd.fujixerox.docuworks.binder");
        MIME_MAP.put("xbm", "image/x-xbitmap");
        MIME_MAP.put("xdf", "application/xcap-diff+xml");
        MIME_MAP.put("xdm", "application/vnd.syncml.dm+xml");
        MIME_MAP.put("xdp", "application/vnd.adobe.xdp+xml");
        MIME_MAP.put("xdssc", "application/dssc+xml");
        MIME_MAP.put("xdw", "application/vnd.fujixerox.docuworks");
        MIME_MAP.put("xenc", "application/xenc+xml");
        MIME_MAP.put("xer", "application/patch-ops-error+xml");
        MIME_MAP.put("xfdf", "application/vnd.adobe.xfdf");
        MIME_MAP.put("xfdl", "application/vnd.xfdl");
        MIME_MAP.put("xht", "application/xhtml+xml");
        MIME_MAP.put("xhtml", "application/xhtml+xml");
        MIME_MAP.put("xhvml", "application/xv+xml");
        MIME_MAP.put("xif", "image/vnd.xiff");
        MIME_MAP.put("xla", "application/vnd.ms-excel");
        MIME_MAP.put("xlam", "application/vnd.ms-excel.addin.macroenabled.12");
        MIME_MAP.put("xlc", "application/vnd.ms-excel");
        MIME_MAP.put("xlf", "application/x-xliff+xml");
        MIME_MAP.put("xlm", "application/vnd.ms-excel");
        MIME_MAP.put("xls", "application/vnd.ms-excel");
        MIME_MAP.put("xlsb", "application/vnd.ms-excel.sheet.binary.macroenabled.12");
        MIME_MAP.put("xlsm", "application/vnd.ms-excel.sheet.macroenabled.12");
        MIME_MAP.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        MIME_MAP.put("xlt", "application/vnd.ms-excel");
        MIME_MAP.put("xltm", "application/vnd.ms-excel.template.macroenabled.12");
        MIME_MAP.put("xltx", "application/vnd.openxmlformats-officedocument.spreadsheetml.template");
        MIME_MAP.put("xlw", "application/vnd.ms-excel");
        MIME_MAP.put("xm", "audio/xm");
        MIME_MAP.put("xml", "application/xml");
        MIME_MAP.put("xo", "application/vnd.olpc-sugar");
        MIME_MAP.put("xop", "application/xop+xml");
        MIME_MAP.put("xpi", "application/x-xpinstall");
        MIME_MAP.put("xpl", "application/xproc+xml");
        MIME_MAP.put("xpm", "image/x-xpixmap");
        MIME_MAP.put("xpr", "application/vnd.is-xpr");
        MIME_MAP.put("xps", "application/vnd.ms-xpsdocument");
        MIME_MAP.put("xpw", "application/vnd.intercon.formnet");
        MIME_MAP.put("xpx", "application/vnd.intercon.formnet");
        MIME_MAP.put("xsl", "application/xml");
        MIME_MAP.put("xslt", "application/xslt+xml");
        MIME_MAP.put("xsm", "application/vnd.syncml+xml");
        MIME_MAP.put("xspf", "application/xspf+xml");
        MIME_MAP.put("xul", "application/vnd.mozilla.xul+xml");
        MIME_MAP.put("xvm", "application/xv+xml");
        MIME_MAP.put("xvml", "application/xv+xml");
        MIME_MAP.put("xwd", "image/x-xwindowdump");
        MIME_MAP.put("xyz", "chemical/x-xyz");
        MIME_MAP.put("xz", "application/x-xz");
        MIME_MAP.put("yang", "application/yang");
        MIME_MAP.put("yin", "application/yin+xml");
        MIME_MAP.put("z", "application/x-compress");
        MIME_MAP.put("Z", "application/x-compress");
        MIME_MAP.put("z1", "application/x-zmachine");
        MIME_MAP.put("z2", "application/x-zmachine");
        MIME_MAP.put("z3", "application/x-zmachine");
        MIME_MAP.put("z4", "application/x-zmachine");
        MIME_MAP.put("z5", "application/x-zmachine");
        MIME_MAP.put("z6", "application/x-zmachine");
        MIME_MAP.put("z7", "application/x-zmachine");
        MIME_MAP.put("z8", "application/x-zmachine");
        MIME_MAP.put("zaz", "application/vnd.zzazz.deck+xml");
        MIME_MAP.put("zip", "application/zip");
        MIME_MAP.put("zir", "application/vnd.zul");
        MIME_MAP.put("zirz", "application/vnd.zul");
        MIME_MAP.put("zmm", "application/vnd.handheld-entertainment+xml");
    }

    // Returns the file extension acording to given path.
    private static String __getExtension(String path) {

        for (int i = path.length() - 1; i >= 0; i--) {
            if (path.charAt(i) == '.') {
                return path.substring(i + 1);
            }
        }

        return "";
    }

    // Returns the mime-type associated with given URI or path
    static String _getMimeType(String uriOrPath) {
        String mime = MIME_MAP.get(__getExtension(uriOrPath));

        if (mime == null) {
            mime = DEFAULT_MIME;
        }

        return mime;
    }

    // Returns a decimal long value represented by given string or a default 
    // value if given string could not be parsed.
    private static long __parseLong(String strLong, long defaultValue) {
        try {
            return Long.parseLong(strLong);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    // Retrive a map containing the request headers associated with given request.
    private static Map<String, String> __getRequestHeaderMap(HttpServletRequest req) {
        Map<String, String> reqHeaders = new LinkedHashMap<>();
        Enumeration<String> headerNames = req.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                Enumeration<String> headerValues = req.getHeaders(headerName);

                if (headerValues != null) {
                    StringBuilder headerValueBuilder = new StringBuilder();
                    int i = 0;
                    while (headerValues.hasMoreElements()) {
                        if (i > 0) {
                            headerValueBuilder.append(", ");
                        }

                        headerValueBuilder.append(headerValues.nextElement());
                        i++;
                    }

                    reqHeaders.put(headerName, headerValueBuilder.toString());
                } else {
                    reqHeaders.put(headerName, "");
                }
            }
        }
        return reqHeaders;
    }

    // Transfers data from given input stream to given output stream using a
    // buffer of given size
    private static void __flush(InputStream is, OutputStream os, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];

        for (int length = 0; (length = is.read(buffer)) > 0;) {
            os.write(buffer, 0, length);
        }
    }

    // Returns the string-representation of the stack trace associated with
    // given throwable.
    private static String __getStackTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));

        return stringWriter.toString();
    }

    // Based on https://github.com/NanoHttpd/nanohttpd/issues/232#issuecomment-180254564
    private static void __serveStream(HttpServletRequest request, HttpServletResponse response, InputStream is, String path, String mime, int bufferSize, long lastModified) throws IOException {
        Map<String, String> reqHeaders = __getRequestHeaderMap(request);

        String queryString = request.getQueryString();
        if (queryString == null) {
            queryString = "";
        }

        // Calculate etag
        String etag = Integer.toHexString((path + lastModified + queryString + is.available()).hashCode());

        // Support (simple) skipping:
        long startFrom = 0;
        long endAt = -1;

        String range = reqHeaders.get("range");
        if (range != null) {
            if (range.startsWith("bytes=")) {
                range = range.substring("bytes=".length());
                int minus = range.indexOf('-');

                if (minus > 0) {
                    startFrom = __parseLong(range.substring(0, minus), 0);
                    endAt = __parseLong(range.substring(minus + 1), -1);
                }
            }
        }

        response.addHeader("Accept-Ranges", "bytes");

        // Change return code and add Content-Range header when skipping is requested
        long fileLen = is.available();
        if (range != null && startFrom >= 0) {
            if (startFrom >= fileLen) {
                response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                response.setContentType("text/plain");
                response.addHeader("Content-Range", "bytes 0-0/" + fileLen);
                response.addHeader("ETag", etag);
            } else {
                if (endAt < 0) {
                    endAt = fileLen - 1;
                }

                long newLen = endAt - startFrom + 1;

                if (newLen < 0) {
                    newLen = 0;
                }

                final long dataLen = newLen;

                is.skip(startFrom);

                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                response.setContentType(mime);
                __flush(is, response.getOutputStream(), bufferSize);
                response.addHeader("Content-Length", "" + dataLen);
                response.addHeader("Content-Range", "bytes " + startFrom + "-" + endAt + "/" + fileLen);
                response.addHeader("ETag", etag);
            }
        } else {
            if (etag.equals(reqHeaders.get("if-none-match"))) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                response.setContentType(mime);
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType(mime);
                __flush(is, response.getOutputStream(), bufferSize);
                response.addHeader("Content-Length", "" + fileLen);
                response.addHeader("ETag", etag);
            }
        }

    }
    // </editor-fold>

    /**
     * Returns a boolean indicating if given request points to a directory.
     *
     * @param req HTTP request.
     * @return boolean indicating if given request points to a directory.
     */
    protected abstract boolean isDirectory(HttpServletRequest req);

    /**
     * Returns a input stream associated with the resource pointed by given
     * request.
     *
     * @param req HTTP request.
     * @return a input stream associated with the resource pointed by given
     * request. If pointed resource does not exists, the implementation shall
     * return null.
     * @throws IOException if an I/O error happened while obtaining the input
     * stream.
     */
    protected abstract InputStream getTargetInputStreamFor(HttpServletRequest req) throws IOException;

    /**
     * Returns the number of milliseconds since UNIX epoch (GMT) representing
     * the timestamp of resource (pointed by given request) last modification.
     * It's assumed that this method will be called only if
     * {@linkplain AbstractFileServlet#getTargetInputStreamFor(javax.servlet.http.HttpServletRequest)}
     * returns a non-null value.
     *
     * @param req HTTP request.
     * @return the number of milliseconds since UNIX epoch (GMT) representing
     * the timestamp of resource (pointed by given request) last modification.
     */
    protected abstract long getTargetLastModifiedFor(HttpServletRequest req);

    /**
     * Returns the content-type associated with the resource pointed by given
     * request. It's assumed that this method will be called only if
     * {@linkplain AbstractFileServlet#getTargetInputStreamFor(javax.servlet.http.HttpServletRequest)}
     * returns a non-null value.
     *
     * @param req HTTP request.
     * @return the content-type associated with the resource pointed by given
     * request.
     */
    protected abstract String getTargetContentTypeFor(HttpServletRequest req);

    /**
     * Returns the default buffer size used for transfers.
     *
     * @return the default buffer size used for transfers. Default
     * implementation returns
     * {@linkplain AbstractFileServlet#DEFAULT_BUFFER_SIZE}.
     */
    protected int getDefaultBufferSize() {
        return DEFAULT_BUFFER_SIZE;
    }

    @Override
    protected final void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        InputStream targetInputStream = null;
        try {
            if (!req.getMethod().toUpperCase().equals("GET")) {
                resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                return;
            }

            String pathInfo = req.getPathInfo();
            if (pathInfo == null) {
                pathInfo = "";
            }

            if (isDirectory(req) && !pathInfo.endsWith("/")) {
                resp.sendRedirect(req.getRequestURI() + "/");
                return;
            }

            targetInputStream = getTargetInputStreamFor(req);

            if (targetInputStream == null) { // <-- Not found
                onNotFound(req, resp);
            } else {
                long lastModified = getTargetLastModifiedFor(req);
                String mime = getTargetContentTypeFor(req);

                __serveStream(req, resp, targetInputStream, pathInfo, mime, getDefaultBufferSize(), lastModified);
            }
        } catch (RuntimeException ex) {
            onUncaughtException(req, resp, ex);
        } finally {
            if (targetInputStream != null) {
                targetInputStream.close();
            }
        }
    }

    /**
     * Called if a resource pointed by given request does not exist (see
     * {@linkplain AbstractFileServlet#getTargetInputStreamFor(javax.servlet.http.HttpServletRequest)}).
     * Default implementation just sends a
     * {@linkplain HttpServletResponse#SC_NOT_FOUND} status.
     *
     * @param req HTTP request.
     * @param resp associated HTTP response.
     */
    protected void onNotFound(HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    /**
     * Called if an uncaught error was detected while processing given request.
     * Default implementation just sends a
     * {@linkplain HttpServletResponse#SC_INTERNAL_SERVER_ERROR} status and the
     * error stack trace embedded in response body.
     *
     * @param request HTTP request.
     * @param response HTTP response.
     * @param error uncaught error.
     * @throws IOException
     */
    protected void onUncaughtException(HttpServletRequest request, HttpServletResponse response, RuntimeException error) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().print(__getStackTrace(error));
    }

}
