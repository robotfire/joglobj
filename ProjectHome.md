This project is a library which will render wavefront models to opengl.

It uses the code and parsing library from
http://www.pixelnerve.com/v/2009/02/06/another-obj-file-loader/

who in turn borrowed code from Fabien Sanglard's wavefront parser and LWJGL rendering library
http://fabiensanglard.net/Mykaruga/index.php

This project is a subproject of http://code.google.com/p/stereosceneviewer/

This project does not have JOGL included or added to the classpath. It assumes that jogl is in the default classpath.

There are several issues with all parts of this project. Specifically, the parser doesn't load transparency, and textures have not been tested as thoroughly as I would like. If you feel you can contribute to this project please contact me, I would be happy to add you as an Admin.