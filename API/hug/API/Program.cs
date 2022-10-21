
using Models;
using Repository;
using Repository.DbContext;
using Repository.Interfaces;
using System.Data.Common;

var builder = WebApplication.CreateBuilder(args);

//Configurations
builder.Services.Configure<AppSettings>(builder.Configuration.GetSection("AppSettings"));

// Add services to the container.
builder.Services.AddSingleton<DapperContext>();
builder.Services.AddScoped<IUnitOfWork, UnitOfWork>();
builder.Services.AddScoped<IUserRepository, UserRepository>(); 
builder.Services.AddScoped<ILocationRepository, LocationRepository>();
builder.Services.AddScoped<IItemRepository, ItemRepository>();







builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseAuthorization();

app.MapControllers();

app.Run();
