const JavaClass = Java.type('java.lang.Class');
/**
 * @template {string} T
 * @param {T} path
 */
export function JavaTypeOrNull(path) {
  const pkg = Java.type(path);
  return (pkg?.['class'] instanceof JavaClass) ? pkg : null;
}

export function throwExp(err) {
  throw err;
}