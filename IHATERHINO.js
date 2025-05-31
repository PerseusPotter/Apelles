import { ___________________________________shhh } from './index';
___________________________________shhh();

Client.scheduleTask(100, () => {
  const JSContextFactory = com.chattriggers.ctjs.engine.langs.js.JSContextFactory;
  const classLoaderF = JSContextFactory.class.getDeclaredField('classLoader');
  classLoaderF.setAccessible(true);
  classLoaderF.get(JSContextFactory.INSTANCE).close();
});