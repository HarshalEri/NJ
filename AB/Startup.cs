using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(AB.Startup))]
namespace AB
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
