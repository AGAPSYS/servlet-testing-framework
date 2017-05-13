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
import javax.servlet.http.HttpServletRequest;

public abstract class EmbeddedFileServlet extends MappedFileServlet {

    // <editor-fold desc="STATIC SCOPE" defaultstate="collapsed">
    private static final String KEY_INPUT_STREAM = EmbeddedFileServlet.class.getName() + ".inputStream";
    private static final String KEY_IO_ERR       = EmbeddedFileServlet.class.getName() + ".ioErr";
    private static final String KEY_INIT         = EmbeddedFileServlet.class.getName() + ".init";
    // </editor-fold>

    @Override
    void _prepareResponseDataOnlyOnce(HttpServletRequest req) {
        if (req.getAttribute(KEY_INIT) == null) {
            
            String pathInfo = req.getPathInfo();
            
            if (pathInfo == null)
                pathInfo = "";
            
            String embeddedPath = getMappedDirPath();

            if (embeddedPath.endsWith("/")) {
                embeddedPath = embeddedPath.substring(0, embeddedPath.length() - 1);
            }

            embeddedPath = embeddedPath + pathInfo;

            if (embeddedPath.endsWith("/")) {
                embeddedPath = embeddedPath.substring(0, embeddedPath.length() - 1);
            }

            InputStream is = EmbeddedFileServlet.class.getResourceAsStream(embeddedPath);

            boolean isDir;

            if (is == null) {
                isDir = false;
            } else {
                try {
                    is.available(); // <-- Workaround to detect embedded directories
                    isDir = false;
                } catch (NullPointerException ex) {
                    isDir = true;
                    try {
                        is.close();
                    } catch (NullPointerException | IOException ignored) {}
                    is = null;
                } catch (IOException ex) {
                    isDir = false;

                    req.setAttribute(KEY_IO_ERR, ex);
                    req.setAttribute(KEY_INIT, true);
                    req.setAttribute(KEY_IS_DIR, isDir);
                    return;
                }
            }
            
            req.setAttribute(KEY_IS_DIR, isDir);

            if (isDir) {
                embeddedPath = String.format("%s/%s", embeddedPath, getIndexFilename());
                is = EmbeddedFileServlet.class.getResourceAsStream(embeddedPath);
            }

            req.setAttribute(KEY_INPUT_STREAM, is);
            req.setAttribute(KEY_MIME, _getMimeType(embeddedPath));
            req.setAttribute(KEY_INIT, true);
        }
    }

    @Override
    protected InputStream getTargetInputStreamFor(HttpServletRequest req) throws IOException {
        _prepareResponseDataOnlyOnce(req);

        IOException ex = (IOException) req.getAttribute(KEY_IO_ERR);

        if (ex != null) {
            throw new IOException(ex);
        }

        return (InputStream) req.getAttribute(KEY_INPUT_STREAM);
    }

    @Override
    protected long getTargetLastModifiedFor(HttpServletRequest req) {
        return 0; // <-- Since resource is embedded, it will never be modified
                  //     during runtime. 
    }
    
    /**
     * Returns the embedded directory path mapped for requests
     * handled by this instance.
     *
     * @return the embedded directory mapped for requests
     * handled by this instance.
     */
    @Override
    protected abstract String getMappedDirPath();

}
